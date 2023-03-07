import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void mkFile(String pathFile) {
        // проверяем есть ли файл и если нет создаем его
        File myFile = new File(pathFile);
        if (myFile.isFile()) {
            try {
                myFile.createNewFile();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void saveGame(String pathFile, GameProgress gameProgress) {
        mkFile(pathFile);
        try (FileOutputStream fos = new FileOutputStream(pathFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {// запишем экземпляр класса в файл
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String pathZip, List<String> list) {

        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathZip))) {
            for (String item : list) {
                File myFile = new File(item);
                FileInputStream fis = new FileInputStream(item);
                ZipEntry entry = new ZipEntry(myFile.getName());
                zout.putNextEntry(entry);// считываем содержимое файла в массив
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);// добавляем содержимое к архиву
                fis.close(); // не прикроешь потом не удаляет файлы
                zout.write(buffer);// закрываем текущую запись для новой записи
                zout.closeEntry();
                // удаляем файл заархивированный из директории
                try {
                    myFile.delete();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static List<String> listFiles(String pathToFiles) {
        // В этом методе соберем в список информацию о файлах в директории
        List<String> list = new ArrayList<>();
        File dir = new File(pathToFiles);
        if (dir.isDirectory()) {
            for (File item : dir.listFiles()) {   // проверим, является ли объект каталогом
                if (item.isDirectory()) {
                } else {
                    list.add(pathToFiles + "//" + item.getName());
                }
            }
        }
        return list;
    }


    public static void main(String[] args) {

        File dir = new File("F://Games//savegames");

        if (dir.isDirectory()) {
            saveGame(dir.getPath() + "//save.dat", new GameProgress(3, 4, 5, 6));
            saveGame(dir.getPath() + "//save.dat", new GameProgress(4, 5, 6, 7));
            saveGame(dir.getPath() + "//save.dat", new GameProgress(5, 6, 7, 8));
            zipFiles(dir.getPath() + "//save.zip", listFiles(dir.getPath()));
        }
    }


}