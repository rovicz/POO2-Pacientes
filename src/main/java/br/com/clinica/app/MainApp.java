package br.com.clinica.app;

import br.com.clinica.dao.PacienteDAO;
import br.com.clinica.model.Paciente;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MainApp extends Application {

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final TableView<Paciente> tableView = new TableView<>();
    private TextField txtId, txtNome, txtCpf, txtTelefone, txtBuscaNome;
    private DatePicker datePickerNascimento;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Gerenciamento de Clientes da Clínica");

        VBox rootLayout = new VBox(20);
        rootLayout.setPadding(new Insets(20));
        rootLayout.getStyleClass().add("root");

        Label lblTitle = new Label("Sistema de Gerenciamento de Clientes da Clínica");
        lblTitle.getStyleClass().add("title-label");

        GridPane formGrid = createFormGrid();

        setupTableView();
        carregarPacientes();

        BorderPane buttonBar = createButtonBar();

        rootLayout.getChildren().addAll(lblTitle, formGrid, tableView, buttonBar);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        Scene scene = new Scene(rootLayout, 900, 750);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> preencherFormulario(newSelection)
        );
    }

    private GridPane createFormGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(15);

        gridPane.add(new Label("ID:"), 0, 0);
        txtId = new TextField();
        txtId.setEditable(false);
        txtId.setDisable(true);
        gridPane.add(txtId, 0, 1);

        gridPane.add(new Label("CPF:"), 0, 2);
        txtCpf = new TextField();
        txtCpf.setPromptText("000.000.000-00");
        gridPane.add(txtCpf, 0, 3);

        gridPane.add(new Label("Telefone:"), 0, 4);
        txtTelefone = new TextField();
        txtTelefone.setPromptText("(00) 00000-0000");
        gridPane.add(txtTelefone, 0, 5);

        gridPane.add(new Label("Nome:"), 1, 0);
        txtNome = new TextField();
        txtNome.setPromptText("Nome completo");
        gridPane.add(txtNome, 1, 1);

        gridPane.add(new Label("Data de Nascimento:"), 1, 2);
        datePickerNascimento = new DatePicker();
        datePickerNascimento.setPromptText("dd/mm/aaaa");
        gridPane.add(datePickerNascimento, 1, 3);

        gridPane.add(new Label("Buscar por Nome:"), 1, 4);
        gridPane.add(createSearchBox(), 1, 5);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(col1, col2);

        return gridPane;
    }

    private HBox createSearchBox() {
        HBox searchBox = new HBox();
        txtBuscaNome = new TextField();
        txtBuscaNome.setPromptText("Digite um nome");
        HBox.setHgrow(txtBuscaNome, Priority.ALWAYS);

        Button btnSearch = new Button();
        btnSearch.setGraphic(new FontIcon(FontAwesomeSolid.SEARCH));
        btnSearch.setId("search-button");
        btnSearch.setOnAction(e -> buscarPaciente());

        searchBox.getChildren().addAll(txtBuscaNome, btnSearch);
        return searchBox;
    }

    private BorderPane createButtonBar() {
        BorderPane buttonBar = new BorderPane();
        buttonBar.setPadding(new Insets(10, 0, 0, 0));

        Button btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setGraphic(new FontIcon(FontAwesomeSolid.PLUS_CIRCLE));
        btnCadastrar.setId("cadastrar-button");
        btnCadastrar.setOnAction(e -> cadastrarPaciente());

        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setGraphic(new FontIcon(FontAwesomeSolid.SYNC_ALT));
        btnAtualizar.setOnAction(e -> atualizarPaciente());

        Button btnListarTodos = new Button("Listar Todos");
        btnListarTodos.setGraphic(new FontIcon(FontAwesomeSolid.LIST_UL));
        btnListarTodos.setOnAction(e -> {
            carregarPacientes();
            limparFormulario();
        });

        HBox rightButtons = new HBox(10, btnAtualizar, btnListarTodos);
        rightButtons.setAlignment(Pos.CENTER_RIGHT);

        buttonBar.setLeft(btnCadastrar);
        buttonBar.setRight(rightButtons);

        return buttonBar;
    }

    private void setupTableView() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Paciente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Paciente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Paciente, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        TableColumn<Paciente, LocalDate> colNascimento = new TableColumn<>("Data de Nascimento");
        colNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));

        TableColumn<Paciente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Paciente, Void> colAcao = new TableColumn<>("Ações");

        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btnExcluir = new Button();
            {
                FontIcon icon = new FontIcon(FontAwesomeSolid.TRASH_ALT);
                icon.setIconColor(new javafx.scene.paint.Color(0.8, 0.2, 0.2, 1.0));
                btnExcluir.setGraphic(icon);
                btnExcluir.getStyleClass().add("delete-button");
                btnExcluir.setOnAction(event -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());
                    excluirPaciente(paciente);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnExcluir);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        tableView.getColumns().addAll(colId, colNome, colCpf, colNascimento, colTelefone, colAcao);
    }

    private void carregarPacientes() {
        try {
            List<Paciente> pacientes = pacienteDAO.listarPacientes();
            tableView.setItems(FXCollections.observableArrayList(pacientes));
        } catch (IOException e) {
            exibirAlerta("Erro de E/S", "Não foi possível ler o arquivo de pacientes: " + e.getMessage());
        }
    }

    private void cadastrarPaciente() {
        try {
            if (txtNome.getText().isEmpty() || txtCpf.getText().isEmpty() || datePickerNascimento.getValue() == null || txtTelefone.getText().isEmpty()) {
                exibirAlerta("Erro de Entrada", "Todos os campos (exceto ID) são obrigatórios.");
                return;
            }
            Paciente novoPaciente = new Paciente(txtNome.getText(), txtCpf.getText(), datePickerNascimento.getValue(), txtTelefone.getText());
            pacienteDAO.salvarPaciente(novoPaciente);
            carregarPacientes();
            limparFormulario();
        } catch (IOException e) {
            exibirAlerta("Erro de Cadastro", "Não foi possível salvar o paciente: " + e.getMessage());
        } catch (Exception e) {
            exibirAlerta("Erro de Entrada", "Verifique os dados informados. Data de nascimento é obrigatória.");
        }
    }

    private void buscarPaciente() {
        String nomeBusca = txtBuscaNome.getText();
        if (nomeBusca == null || nomeBusca.trim().isEmpty()) {
            carregarPacientes();
            return;
        }
        try {
            List<Paciente> resultado = pacienteDAO.buscarPacientePorNome(nomeBusca);
            tableView.setItems(FXCollections.observableArrayList(resultado));
        } catch (IOException e) {
            exibirAlerta("Erro de Busca", "Ocorreu um erro ao buscar pacientes.");
        }
    }

    private void atualizarPaciente() {
        try {
            Paciente pacienteAtualizado = new Paciente(Integer.parseInt(txtId.getText()), txtNome.getText(), txtCpf.getText(), datePickerNascimento.getValue(), txtTelefone.getText());
            pacienteDAO.atualizarPaciente(pacienteAtualizado);
            carregarPacientes();
            limparFormulario();
        } catch (NumberFormatException e) {
            exibirAlerta("Erro de Atualização", "Nenhum paciente selecionado para atualizar. Selecione na tabela primeiro.");
        } catch (IOException e) {
            exibirAlerta("Erro de Atualização", "Não foi possível atualizar o paciente: " + e.getMessage());
        }
    }

    private void excluirPaciente(Paciente paciente) {
        if (paciente == null) {
            exibirAlerta("Erro de Exclusão", "Paciente inválido.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir o paciente " + paciente.getNome() + "?", ButtonType.YES, ButtonType.NO);
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    pacienteDAO.excluirPaciente(paciente.getId());
                    carregarPacientes();
                    limparFormulario();
                } catch (IOException e) {
                    exibirAlerta("Erro de Exclusão", "Não foi possível excluir o paciente: " + e.getMessage());
                }
            }
        });
    }

    private void preencherFormulario(Paciente paciente) {
        if (paciente != null) {
            txtId.setText(String.valueOf(paciente.getId()));
            txtNome.setText(paciente.getNome());
            txtCpf.setText(paciente.getCpf());
            datePickerNascimento.setValue(paciente.getDataNascimento());
            txtTelefone.setText(paciente.getTelefone());
        }
    }

    private void limparFormulario() {
        txtId.clear();
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        datePickerNascimento.setValue(null);
        txtBuscaNome.clear();
        tableView.getSelectionModel().clearSelection();
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("root");

        alert.showAndWait();
    }
}