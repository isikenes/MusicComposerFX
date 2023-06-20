module com.isikenes.musiccomposerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires jfugue;


    opens com.isikenes.musiccomposerfx to javafx.fxml;
    exports com.isikenes.musiccomposerfx;
}