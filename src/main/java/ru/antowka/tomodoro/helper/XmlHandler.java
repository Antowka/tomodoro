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

    private Path pathFile;
    private Class<T> clazz;


    public XmlHandler(String filePath, Class<T> clazz) {
        this.clazz = clazz;
        this.pathFile = Paths.get("settings"+ System.getProperty("file.separator") + filePath);
    }

    /**
     * Load object from xml
     *
     * @return
     */
    public T loadDataFromFile() {
        try {

            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            return (T)um.unmarshal(pathFile.toFile());

        } catch (Exception e) { // catches ANY exception

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + pathFile.toString());
            alert.showAndWait();
        }

        return null;
    }

    /**
     * Save object to xml file
     *
     * @param object
     */
    public void saveDataToFile(T object) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(clazz);

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(object, pathFile.toFile());

        } catch (Exception e) { // catches ANY exception

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + pathFile.toString());

            alert.showAndWait();
        }
    }
}
