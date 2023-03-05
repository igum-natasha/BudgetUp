package com.example.budgetup;

import android.view.View;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
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
    FileInputStream file = new FileInputStream(new File(filename));
    Map<Integer, List<String>> data = new HashMap<>();
    int i = 0;
    Sheet sheet;

    if (FilenameUtils.getExtension(filename).equals("xls")) {
      POIFSFileSystem f = new POIFSFileSystem(file);
      try (HSSFWorkbook workbook = new HSSFWorkbook(f)) {
        sheet = workbook.getSheetAt(0);
      }
    } else {
      try (Workbook workbook = new XSSFWorkbook(file)) {
        sheet = workbook.getSheetAt(0);
      }
    }
    for (Row row : sheet) {
      data.put(i, new ArrayList<String>());
      for (int j = 0; j < row.getLastCellNum(); j++) {
        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        switch (cell.getCellType()) {
          case STRING:
            data.get(i).add(cell.getStringCellValue());
            break;
          case NUMERIC:
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
    // temp solution start
    Map<String, String[]> new_data = new HashMap<>();
    new_data.put("communication", new String[] {"ЖКХ, связь, интернет ", "Мобильная связь"});
    new_data.put("restaurants", new String[] {"Фастфуд", "Рестораны", "Рестораны и кафе"});
    new_data.put("clothes", new String[] {"Одежда и аксессуары", "Одежда и обувь"});
    new_data.put("services", new String[] {"Онлайн-маркеты", "Сервис", "Различные товары"});
    new_data.put("food", new String[] {"Супермаркеты"});
    new_data.put("gift", new String[] {"Развлечения и хобби", "Детские товары"});
    new_data.put("health", new String[] {"Аптеки", "Здоровье и красота"});
    new_data.put("house", new String[] {"Дом и ремонт", "Все для дома", "ЖКХ"});
    new_data.put("pets", new String[] {"Животные"});
    new_data.put("sports", new String[] {"Спорттовары"});
    new_data.put("transport", new String[] {"Транспорт", "Путешествия", "Авиабилеты"});
    new_data.put(
        "others",
        new String[] {
          "Переводы людям", "Снятие наличных", "Наличные", "Подписки", "Переводы", "Остальное"
        });
    new_data.put("salary", new String[] {"Зарплата"});
    // temp solution end
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
            if (filename.contains("outcome")) {
              value = data.get(i).get(j);
            } else {
              value = data.get(i).get(j);
            }
            expense.setValue(value);
            break;
          case "Номер счета/карты зачисления":
          case "Номер счета/карты списания":
          case "Номер карты":
            value = data.get(i).get(j);
            try {
              value = value.substring(value.length() - 4);
            } catch (NullPointerException e) {
              value = "No card";
            }
            //                        expense.setCard(value);
            break;
          case "Категория":
            value = null;
            for (String key : new_data.keySet()) {
              if (Arrays.asList(new_data.get(key)).contains(data.get(i).get(j))) {
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
      db.expenseDao().insertExpense(expense);
    }
  }
}
