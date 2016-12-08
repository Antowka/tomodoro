package ru.antowka.tomodoro.helper;

import javafx.scene.control.Alert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handle XML file to Object and Object to XML
 */
public class XmlHandler<T> {

    private T type;

    /**
     * Load object from xml
     *
     * @param pathToFile
     * @return
     */
    public T loadDataFromFile(String pathToFile) {
        try {
            JAXBContext context = JAXBContext.newInstance(type.getClass());
            Unmarshaller um = context.createUnmarshaller();

            Path pathFile = Paths.get(pathToFile);

            // Чтение XML из файла и демаршализация.
            return (T)um.unmarshal(pathFile.toFile());

        } catch (Exception e) { // catches ANY exception

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + pathToFile);
            alert.showAndWait();
        }

        return null;
    }

    /**
     * Save object to xml file
     *
     * @param pathToFile
     * @param object
     */
    public void saveDataToFile(String pathToFile, T object) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(type.getClass());

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Path pathFile = Paths.get(pathToFile);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(object, pathFile.toFile());

        } catch (Exception e) { // catches ANY exception

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + pathToFile);

            alert.showAndWait();
        }
    }
}
