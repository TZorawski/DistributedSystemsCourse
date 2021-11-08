/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author thais
 */
public class Matricula {
    private int RA;
    private String cod_disciplina;
    private int ano;
    private int semestre;
    private float nota;
    private int faltas;
    
    public Matricula () {}

    public Matricula(int RA, String cod_disciplina, int ano, int semestre) {
        this.RA = RA;
        this.cod_disciplina = cod_disciplina;
        this.ano = ano;
        this.semestre = semestre;
    }

    public Matricula(int RA, String cod_disciplina, int ano, int semestre, float nota, int faltas) {
        this.RA = RA;
        this.cod_disciplina = cod_disciplina;
        this.ano = ano;
        this.semestre = semestre;
        this.nota = nota;
        this.faltas = faltas;
    }

    public int getRA() {
        return RA;
    }

    public void setRA(int RA) {
        this.RA = RA;
    }

    public String getCod_disciplina() {
        return cod_disciplina;
    }

    public void setCod_disciplina(String cod_disciplina) {
        this.cod_disciplina = cod_disciplina;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    @Override
    public String toString() {
        return "Matricula{" + "RA=" + RA + ", cod_disciplina=" + cod_disciplina + ", ano=" + ano + ", semestre=" + semestre + ", nota=" + nota + ", faltas=" + faltas + '}';
    }
}
