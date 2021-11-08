package dao;

import model.Disciplina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author André  Schwerz
 */
public class DisciplinaDAO extends DbConnection{
    private Connection conn;
    private final String sqlInsert = "INSERT INTO Disciplina (codigo, nome, professor, cod_curso) VALUES (?,?,?,?)";
    private final String sqlUpdate = "UPDATE Disciplina SET nome = ?, professor = ?, cod_curso = ? WHERE codigo = ? ";
    private final String sqlRemove = "DELETE FROM Disciplina WHERE codigo = ?";
    private final String sqlList   = "SELECT codigo, nome, professor, cod_curso FROM Disciplina ORDER BY nome";
    private final String sqlFind   = "SELECT codigo, nome, professor, cod_curso FROM Disciplina WHERE codigo = ?";

    public void insert(Disciplina disciplina) throws SQLException{
        conn = connect();
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(sqlInsert);
            ps.setString(1, disciplina.getCodigo());
            ps.setString(2, disciplina.getNome());
            ps.setString(3, disciplina.getProfessor());
            ps.setInt(4, disciplina.getCod_curso());
            ps.execute();
        }
        finally{
            ps.close();
            close(conn);
        }
        
    }
    
    public void update(Disciplina disciplina) throws SQLException{
        PreparedStatement ps = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlUpdate);
            ps.setString(1, disciplina.getNome());
            ps.setString(2, disciplina.getProfessor());
            ps.setInt(3, disciplina.getCod_curso());
            ps.setString(4, disciplina.getCodigo());
            ps.execute();
        }
        finally{
            ps.close();
            close(conn);
        }
    }
    
    public void remove(String codigo) throws SQLException{
        PreparedStatement ps = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlRemove);
            ps.setString(1, codigo);
            ps.execute();
        }
        finally{
            ps.close();
            close(conn);
        }

    }
    
    public ArrayList<Disciplina> list() throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlList);
            rs = ps.executeQuery();
            ArrayList<Disciplina> list = new ArrayList<>();
            Disciplina disciplina;
            while (rs.next()){
                disciplina = new Disciplina();
                disciplina.setCodigo(rs.getString("codigo"));
                disciplina.setNome(rs.getString("nome"));
                disciplina.setProfessor(rs.getString("professor"));
                disciplina.setCod_curso(rs.getInt("cod_curso"));
                list.add(disciplina);
            }
            return list;
        }
        finally{
            rs.close();
            ps.close();
            close(conn);
        }
    }

    public Disciplina find(String codigo)throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlFind);
            ps.setString(1, codigo);

            rs = ps.executeQuery();
            Disciplina disciplina = null ;

            if (rs.next()){
                disciplina = new Disciplina();
                disciplina.setCodigo(rs.getString("codigo"));
                disciplina.setNome(rs.getString("nome"));
                disciplina.setProfessor(rs.getString("professor"));
                disciplina.setCod_curso(rs.getInt("cod_curso"));
            }
            return disciplina;
        }
        finally{
            rs.close();
            ps.close();
            close(conn);
        }       
    }
}