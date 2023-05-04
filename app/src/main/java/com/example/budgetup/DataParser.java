package com.example.budgetup;

import android.view.View;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataParser {
  String filename;

  public DataParser(String name) {
    filename = name;
  }

  public void parseFile(View view) throws IOException, ParseException {
    FileInputStream file = new FileInputStream(filename);
    Map<Integer, List<String>> data = new HashMap<>();
    int i = 0;
    Sheet sheet;
    String[] ext = filename.split("\\.");

    if (ext[ext.length - 1].equals("xls")) {
      POIFSFileSystem f = new POIFSFileSystem(file);
      HSSFWorkbook workbook = new HSSFWorkbook(f);
      sheet = workbook.getSheetAt(0);
    } else {
      XSSFWorkbook workbook = new XSSFWorkbook(file);
      sheet = workbook.getSheetAt(0);
    }
    for (Row row : sheet) {
      data.put(i, new ArrayList<String>());
      for (int j = 0; j < row.getLastCellNum(); j++) {
        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        switch (cell.getCellType()) {
          case Cell.CELL_TYPE_STRING:
            data.get(i).add(cell.getStringCellValue());
            break;
          case Cell.CELL_TYPE_NUMERIC:
            data.get(i).add(cell.getNumericCellValue() + "");
            break;
          default:
            data.get(i).add(null);
            break;
        }
      }
      System.out.print(data.get(i) + "\n");
      i++;
    }
    updateDB(data, view);
  }

  private void updateDB(Map<Integer, List<String>> data, View view) throws ParseException {
    Map<String, Integer> new_data = new HashMap<>();
    new_data.put("phone", R.array.phone);
    new_data.put("cafe", R.array.cafe);
    new_data.put("clothes", R.array.clothes);
    new_data.put("services", R.array.services);
    new_data.put("food", R.array.food);
    new_data.put("gifts", R.array.gifts);
    new_data.put("health", R.array.health);
    new_data.put("house", R.array.house);
    new_data.put("pets", R.array.pets);
    new_data.put("sports", R.array.sports);
    new_data.put("transport", R.array.transport);
    new_data.put("others", R.array.others);
    new_data.put("salary", R.array.salary);

    List<String> headers = data.get(0);
    Locale locale = new Locale("ru");
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    SimpleDateFormat formatRussian = new SimpleDateFormat("dd MMMM yyyy, HH:mm", locale);
    Date date;
    String value;
    int image;
    AppDatabase db = AppDatabase.build(view.getContext());
    User user = db.userDao().getByStatus("online");
    for (int i = 1; i < data.size(); i++) {
      Expense expense = new Expense();
      expense.setId(UUID.randomUUID().toString());
      expense.setUserEmail(user.getEmail());
      expense.setPayment("Credit card");
      for (int j = 0; j < headers.size(); j++) {
        switch (headers.get(j)) {
          case "Дата операции":
          case "Дата":
            try {
              date = format.parse(data.get(i).get(j));
            } catch (ParseException e) {
              date = formatRussian.parse(data.get(i).get(j));
            }
            expense.setDate(date.getTime());
            break;
          case "Валюта":
          case "Валюта платежа":
            expense.setCurrency(data.get(i).get(j));
            break;
          case "Описание":
            expense.setNote(data.get(i).get(j).trim().replaceAll(" +", " "));
            break;
          case "Сумма платежа":
          case "Сумма":
            value = data.get(i).get(j).replace("-", "").trim();
            expense.setValue(value);
            break;
          case "Номер счета/карты зачисления":
          case "Номер счета/карты списания":
          case "Номер карты":
            value = data.get(i).get(j);
            try {
              value = value.substring(value.length() - 4);
            } catch (NullPointerException e) {
              value = "None";
            }
            expense.setCardNum(value);
            break;
          case "Категория":
            value = null;
            for (String key : new_data.keySet()) {
              List<String> array =
                  Arrays.asList(view.getResources().getStringArray(new_data.get(key)));
              if (array.contains(data.get(i).get(j))) {
                value = key;
                break;
              }
            }
            value = value == null ? "others" : value;
            expense.setCategory(value);
            image =
                view.getResources()
                    .getIdentifier(value, "drawable", view.getContext().getPackageName());
            expense.setImage(image);
            break;
          default:
            break;
        }
      }
      if (!expense.getCategory().equals("salary")) {
        String new_value = "-" + expense.getValue();
        expense.setValue(new_value);
      }
      List<Expense> expenseList = db.expenseDao().getAll();
      for (Expense exp : expenseList) {
        if (!(exp.getDate() == expense.getDate()
            && exp.getValue().equals(expense.getValue())
            && exp.getCardNum().equals(expense.getCardNum()))) {
          db.expenseDao().insertExpense(expense);
        }
      }
    }
  }
}
