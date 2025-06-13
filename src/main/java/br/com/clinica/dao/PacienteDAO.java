package br.com.clinica.dao;

import br.com.clinica.model.Paciente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PacienteDAO {

    private static final Path ARQUIVO_CSV = Paths.get("pacientes.csv");

    public void salvarPaciente(Paciente paciente) throws IOException {
        List<Paciente> pacientes = listarPacientes();
        int novoId = gerarNovoId(pacientes);
        paciente.setId(novoId);

        try (BufferedWriter writer = Files.newBufferedWriter(ARQUIVO_CSV, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            writer.write(paciente.toString());
            writer.newLine();
        }
    }

    public List<Paciente> listarPacientes() throws IOException {
        List<Paciente> pacientes = new ArrayList<>();
        if (!Files.exists(ARQUIVO_CSV)) {
            return pacientes;
        }

        try (BufferedReader reader = Files.newBufferedReader(ARQUIVO_CSV)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] dados = linha.split(",");
                Paciente p = new Paciente(
                        Integer.parseInt(dados[0]),
                        dados[1],
                        dados[2],
                        LocalDate.parse(dados[3]),
                        dados[4]
                );
                pacientes.add(p);
            }
        }
        return pacientes;
    }

    public List<Paciente> buscarPacientePorNome(String nome) throws IOException {
        return listarPacientes().stream()
                .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void atualizarPaciente(Paciente pacienteAtualizado) throws IOException {
        List<Paciente> pacientes = listarPacientes();
        List<Paciente> pacientesAtualizados = pacientes.stream()
                .map(p -> p.getId() == pacienteAtualizado.getId() ? pacienteAtualizado : p)
                .collect(Collectors.toList());

        escreverTodosPacientes(pacientesAtualizados);
    }

    public void excluirPaciente(int id) throws IOException {
        List<Paciente> pacientes = listarPacientes();
        List<Paciente> pacientesRestantes = pacientes.stream()
                .filter(p -> p.getId() != id)
                .collect(Collectors.toList());

        escreverTodosPacientes(pacientesRestantes);
    }

    private void escreverTodosPacientes(List<Paciente> pacientes) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(ARQUIVO_CSV, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Paciente p : pacientes) {
                writer.write(p.toString());
                writer.newLine();
            }
        }
    }

    private int gerarNovoId(List<Paciente> pacientes) {
        return pacientes.stream()
                .max(Comparator.comparingInt(Paciente::getId))
                .map(p -> p.getId() + 1)
                .orElse(1);
    }
}