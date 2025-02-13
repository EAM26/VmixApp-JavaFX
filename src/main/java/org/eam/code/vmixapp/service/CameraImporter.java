package org.eam.code.vmixapp.service;

import org.eam.code.vmixapp.util.Alarm;
import org.eam.code.vmixapp.util.DOMParser;
import org.eam.code.vmixapp.util.SelectedSequence;
import org.eam.code.vmixapp.util.Validation;

import java.util.List;

public class CameraImporter {

    private final MyCameraService myCameraService;

    String xmlResponse = "<vmix><version>28.0.0.36</version><edition>4K</edition><preset>C:\\Users\\synco\\AppData\\Roaming\\last.vmix</preset><inputs>"
            + "<input key=\"4d64cadf-aa54-461b-8e78-97ea41108da2\" number=\"1\" type=\"Capture\" title=\"C1\" shortTitle=\"C1\" state=\"Running\" position=\"0\" duration=\"0\" loop=\"False\">C1</input>"
            + "<input key=\"652898f9-a524-40ba-a21f-c0b28d512a0c\" number=\"2\" type=\"Colour\" title=\"C2\" shortTitle=\"C2\" state=\"Paused\" position=\"0\" duration=\"0\" loop=\"False\">C2</input>"
            + "<input key=\"bbc3da0b-09ee-4ed7-90b4-97ba106f872a\" number=\"3\" type=\"Virtual\" title=\"PTZ - C1-TOTAAL\" shortTitle=\"C1-TOTAAL\" state=\"Paused\" position=\"0\" duration=\"0\" loop=\"False\">PTZ - C1-TOTAAL</input>"
            + "<input key=\"db907683-92f5-4a59-854f-aa48c529e6c3\" number=\"4\" type=\"Virtual\" title=\"PTZ - C1-HOUT\" shortTitle=\"C1-HOUT\" state=\"Paused\" position=\"0\" duration=\"0\" loop=\"False\">PTZ - C1-HOUT</input>"
            + "<input key=\"680decec-39e0-47e4-8c73-9049ae549d01\" number=\"5\" type=\"Image\" title=\"cam10-Piano-Superclose.jpg\" shortTitle=\"cam10-Piano-Superclose.jpg\" state=\"Paused\" position=\"0\" duration=\"0\" loop=\"False\">cam10-Piano-Superclose.jpg</input>"
            + "</inputs></vmix>";

    public CameraImporter() {
        this.myCameraService = new MyCameraService();
    }

    public void importCameras() {

        DOMParser dp = new DOMParser();
        List<String> vmixCamList = dp.readXML(xmlResponse);
        if (vmixCamList != null && !vmixCamList.isEmpty()) {

            try {
                checkNameForSpace(vmixCamList);
                camNameIsDuplicate(vmixCamList);
                camListCreator(vmixCamList);
                showListNames(vmixCamList);
            } catch (RuntimeException e) {
                Alarm.showError("Import error: " + e.getMessage());
            }
        }

    }

    public void showListNames(List<String> camList) {
        for (String camName : camList) {
            System.out.println(camName);
        }
    }

    public void checkNameForSpace(List<String> camList) {
        for (String camName : camList) {
            if (camName.contains(" ")) {
                throw new IllegalArgumentException("Import error. Name contains space.");
            }
        }
    }

    public void camNameIsDuplicate(List<String> camList) {
        for (String camName : camList) {
            if (camNameExists(camName)) {
                System.out.println(camName);
                throw new IllegalArgumentException("Import error. Name must be unique.");
            }
        }
    }

    private boolean camNameExists(String camName) {
        return Validation.existsInTable("cameras", "name", camName, "SeqId", SelectedSequence.getSelectedSequence().getId());
    }

    private void camListCreator(List<String> camList) {
//        int ref = 100;
//        for (String camName: camList) {
//            myCameraService.createCam(String.valueOf(ref++), camName, SelectedSequence.getSelectedSequence());
//        }
    }


}
