/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dao.CursoDAO;
import dao.DisciplinaDAO;
import dao.AlunoDAO;
import dao.MatriculaDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Curso;
import model.Disciplina;
import model.Aluno;
import model.Matricula;

/**
 *
 * @author thais
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        testeDeOperacoes("matricula");
    }

    private static void testeDeOperacoes(String classe) {
        if (classe.contentEquals("curso")) {
            Curso curso = new Curso(004, "Engenharia de Alimentos");
            CursoDAO cdao = new CursoDAO();
            try {
                // Insert
                cdao.insert(curso);
                
                // Find
                System.out.println("Curso procurado: " + cdao.find(4));
                // Update
                curso.setNome("Engenharia Alimentícia");
                cdao.update(curso);
                // List
                System.out.println("Lista de cursos: \n" + cdao.list());
                // Remove
                cdao.remove(4);
                System.out.println("Curso procurado: " + cdao.find(4));
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (classe.contentEquals("disciplina")) {
            Disciplina disciplina = new Disciplina("ICO6CG", "Computação Gráfica", "Professor Um", 1);
            DisciplinaDAO cdao = new DisciplinaDAO();
            try {
                // Insert
                cdao.insert(disciplina);
                
                // Find
                System.out.println("Disciplina procurada: " + cdao.find("ICO6CG").getNome());

                // Update
                disciplina.setNome("Estudo de Processamento Paralelo");
                cdao.update(disciplina);

                // List
                System.out.println("Lista de disciplinas: \n" + cdao.list());

                // Remove
                cdao.remove("ICO6CG");
                System.out.println("Disciplina procurada: " + cdao.find("ICO6CG"));
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (classe.contentEquals("aluno")) {
            Aluno aluno = new Aluno(1511556, "Thais Zorawski", 7, 1);
            AlunoDAO cdao = new AlunoDAO();
            try {
                // Insert
                cdao.insert(aluno);
                
                // Find
                System.out.println("Aluno procurado: " + cdao.find(1511556).getNome());

                // Update
                aluno.setNome("Estudo de Processamento Paralelo");
                cdao.update(aluno);

                // List
                System.out.println("Lista de alunos: \n" + cdao.list());

                // Remove
                cdao.remove(1511556);
                System.out.println("Aluno procurado: " + cdao.find(1511556));
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (classe.contentEquals("matricula")) {
            Matricula matricula = new Matricula(1511557, "ICO6PP", 2020, 1);
            MatriculaDAO cdao = new MatriculaDAO();
            try {
                // Insert
                cdao.insert(matricula);
                
                // Find
                System.out.println("Matrícula procurada: " + cdao.find(1511557, "ICO6PP", 2020, 1));

                // Update
                matricula.setNota(6.5f);
                matricula.setFaltas(5);
                cdao.update(matricula);

                // List
                System.out.println("Lista de matriculas: \n" + cdao.list());

                // Remove
                cdao.remove(1511557, "ICO6PP", 2020, 1);
                System.out.println("Matricula procurada: " + cdao.find(1511557, "ICO6PP", 2020, 1));
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
