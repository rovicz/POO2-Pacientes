# 🏥 Sistema de Gerenciamento de Clientes - Clínica

![Java](https://img.shields.io/badge/Java-17%2B-blue?style=for-the-badge&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-21-orange?style=for-the-badge&logo=openjfx)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red?style=for-the-badge&logo=apachemaven)

Este projeto é uma aplicação desktop desenvolvida em **Java** com a framework **JavaFX** para o gerenciamento de pacientes de uma clínica. O sistema oferece uma interface gráfica moderna e intuitiva para realizar as operações básicas de cadastro, consulta, atualização e exclusão de registros.

## ✨ Funcionalidades Principais

* ➕ **Cadastro de Pacientes:** Adicione novos pacientes com informações essenciais (Nome, CPF, Data de Nascimento, Telefone).
* 👁️ **Visualização e Busca:** Visualize a lista completa de pacientes em uma tabela e realize buscas dinâmicas por nome.
* 🔄 **Atualização de Dados:** Selecione um paciente na tabela para carregar seus dados e atualizá-los facilmente.
* 🗑️ **Exclusão Segura:** Remova registros do sistema através de um botão na própria linha da tabela, com um diálogo de confirmação para evitar exclusões acidentais.
* 🎨 **Interface Moderna:** UI com tema escuro, construída com JavaFX e estilizada com CSS para uma experiência de usuário agradável.
* 💾 **Persistência de Dados:** Todas as informações são salvas localmente em um arquivo `.csv`, garantindo que os dados não se percam ao fechar a aplicação.

## 💻 Tecnologias Utilizadas

* **Java 17+:** Linguagem principal da aplicação.
* **[JavaFX 21](https://openjfx.io/):** Framework para a construção da interface gráfica.
* **[Maven](https://maven.apache.org/):** Gerenciamento de dependências e build do projeto.
* **CSS:** Utilizado para a estilização customizada e criação do tema escuro.
* **[Ikonli](https://kordamp.org/ikonli/):** Biblioteca para a inclusão de ícones vetoriais na interface.

## 🏛️ Arquitetura

O projeto utiliza o padrão **DAO (Data Access Object)** para separar a lógica de negócios da lógica de acesso aos dados. Isso torna o código mais limpo, organizado e fácil de manter, isolando a forma como os dados são lidos e escritos (neste caso, de um arquivo CSV).

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **JDK 17** ou superior.
* **Maven** configurado no seu sistema ou integrado à sua IDE.

### Passos
1.  Clone o repositório:
    ```bash
    git clone [https://github.com/seu-usuario/seu-repositorio.git](https://github.com/seu-usuario/seu-repositorio.git)
    ```
2.  Abra o projeto na sua IDE preferida (IntelliJ, Eclipse, etc.) como um projeto Maven.
3.  Aguarde a IDE baixar todas as dependências listadas no arquivo `pom.xml`.
4.  Execute a classe principal `MainApp.java` localizada em `src/main/java/br/com/clinica/app/`.

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
