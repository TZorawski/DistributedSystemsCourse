package dao;

import model.Matricula;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author André  Schwerz
 */
public class MatriculaDAO extends DbConnection{
    private Connection conn;
    private final String sqlInsert = "INSERT INTO Matricula (ra, cod_disciplina, ano, semestre) VALUES (?,?,?,?)";
    private final String sqlUpdate = "UPDATE Matricula SET nota = ?, faltas = ? WHERE ra = ? AND cod_disciplina = ? AND ano = ? AND semestre = ? ";
    private final String sqlRemove = "DELETE FROM Matricula WHERE ra = ? AND cod_disciplina = ? AND ano = ? AND semestre = ?";
    private final String sqlList   = "SELECT ra, cod_disciplina, ano, semestre, nota, faltas FROM Matricula ORDER BY ano";
    private final String sqlFind   = "SELECT ra, cod_disciplina, ano, semestre, nota, faltas FROM Matricula WHERE ra = ? AND cod_disciplina = ? AND ano = ? AND semestre = ?";

    public void insert(Matricula matricula) throws SQLException{
        System.out.println("Iniciando");
        conn = connect();
        System.out.println("Conectado");
        PreparedStatement ps = null;
        try{
            System.out.println("Utilizando con");
            ps = conn.prepareStatement(sqlInsert);
            System.out.println("Construindo query1");
            ps.setInt(1, matricula.getRA());
            System.out.println("Construindo query2");
            ps.setString(2, matricula.getCod_disciplina());
            ps.setInt(3, matricula.getAno());
            ps.setInt(4, matricula.getSemestre());
            System.out.println("Executando");
            ps.execute();
            System.out.println("executou");
        }
        finally{
            System.out.println("finalizando");
            ps.close();
            close(conn);
            System.out.println("finalizado");
        }
        
    }
    
    public void update(Matricula matricula) throws SQLException{
        PreparedStatement ps = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlUpdate);
            ps.setFloat(1, matricula.getNota());
            ps.setInt(2, matricula.getFaltas());
            ps.setInt(3, matricula.getRA());
            ps.setString(4, matricula.getCod_disciplina());
            ps.setInt(5, matricula.getAno());
            ps.setInt(6, matricula.getSemestre());
            ps.execute();
        }
        finally{
            ps.close();
            close(conn);
        }
    }
    
    public void remove(int RA, String cod_disciplina, int ano, int semestre) throws SQLException{
        PreparedStatement ps = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlRemove);
            ps.setInt(1, RA);
            ps.setString(2, cod_disciplina);
            ps.setInt(3, ano);
            ps.setInt(4, semestre);
            ps.execute();
        }
        finally{
            ps.close();
            close(conn);
        }

    }
    
    public ArrayList<Matricula> list() throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlList);
            rs = ps.executeQuery();
            ArrayList<Matricula> list = new ArrayList<>();
            Matricula matricula;
            while (rs.next()){
                matricula = new Matricula();
                matricula.setRA(rs.getInt("ra"));
                matricula.setCod_disciplina(rs.getString("cod_disciplina"));
                matricula.setAno(rs.getInt("ano"));
                matricula.setSemestre(rs.getInt("semestre"));
                matricula.setNota(rs.getFloat("nota"));
                matricula.setFaltas(rs.getInt("faltas"));
                list.add(matricula);
            }
            return list;
        }
        finally{
            rs.close();
            ps.close();
            close(conn);
        }
    }

    public Matricula find(int RA, String cod_disciplina, int ano, int semestre)throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlFind);
            ps.setInt(1, RA);
            ps.setString(2, cod_disciplina);
            ps.setInt(3, ano);
            ps.setInt(4, semestre);

            rs = ps.executeQuery();
            Matricula matricula = null ;

            if (rs.next()){
                matricula = new Matricula();
                matricula.setRA(rs.getInt("ra"));
                matricula.setCod_disciplina(rs.getString("cod_disciplina"));
                matricula.setAno(rs.getInt("ano"));
                matricula.setSemestre(rs.getInt("semestre"));
                matricula.setNota(rs.getFloat("nota"));
                matricula.setFaltas(rs.getInt("faltas"));
            }
            return matricula;
        }
        finally{
            rs.close();
            ps.close();
            close(conn);
        }       
    }
}