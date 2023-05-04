package com.example.budgetup;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

public class GoogleDriveBackup {
  private Drive driver;
  private String currentDBPath;

  public GoogleDriveBackup(Drive driver, String dbPath) {
    this.currentDBPath = dbPath;
    this.driver = driver;
  }

  public void upload() throws IOException {
    File storageFile = new File();
    storageFile.setParents(Collections.singletonList("appDataFolder"));
    storageFile.setName("budgetdb");

    java.io.File filePath = new java.io.File(currentDBPath);
    FileContent mediaContent = new FileContent("", filePath);
    try {
      File file = driver.files().create(storageFile, mediaContent).execute();
      System.out.printf("Filename: %s File ID: %s \n", file.getName(), file.getId());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void download() throws IOException {
    java.io.File dir = new java.io.File(currentDBPath);
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (String child : children) {
          new java.io.File(dir, child).delete();
      }
    }

    FileList files =
            driver
                    .files()
                    .list()
                    .setSpaces("appDataFolder")
                    .setFields("nextPageToken, files(id, name, createdTime)")
                    .setPageSize(10)
                    .execute();
    for (File file : files.getFiles()) {
      System.out.printf(
              "Found file: %s (%s) %s\n", file.getName(), file.getId(), file.getCreatedTime());
      if (file.getName().equals("budgetdb")) {
        OutputStream outputStream = new FileOutputStream(currentDBPath);
        driver.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
        break;
      }
    }
  }
}
