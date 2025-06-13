module clinica {
    // Requer os módulos do JavaFX necessários
    requires javafx.controls;

    // 👇 ADICIONE ESTAS DUAS LINHAS
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    // Exporta o pacote principal da aplicação
    exports br.com.clinica.app;

    // Abre o pacote do modelo para o JavaFX
    opens br.com.clinica.model to javafx.base;
}