module purrinha {
  requires javafx.controls;
  requires javafx.fxml;
  requires transitive javafx.graphics;
  requires kotlin.stdlib;

  opens purrinha to javafx.fxml;  
  exports purrinha;              
}
