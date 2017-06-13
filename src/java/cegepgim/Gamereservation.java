/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cegepgim;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import utilities.create_connection;

/**
 * REST Web Service
 *
 * @author Yad
 */
@Path("gamereservation")
public class Gamereservation {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Gamereservation
     */
    public static String status = null;
    JSONObject obj = new JSONObject();
    public Gamereservation() {
    }

    /**
     * Retrieves representation of an instance of cegepgim.Gamereservation
     * @return an instance of java.lang.String
     */
    
    @Path("signup&{Uname}&{Ulastn}&{Uid}&{Uemail}&{Unumber}&{Upass}&{datebirth}")
    @GET
    @Produces("application/json")
    public String signup(@PathParam("Uname") String name, @PathParam("Ulastn") String lastn, @PathParam("Uid") String id, @PathParam("Uemail") String useremail, @PathParam("Unumber") String number, @PathParam("Upass") String pass, @PathParam("datebirth") String birthdate) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();
            String sql1 = "INSERT INTO USER_INFO (FIRSTNAME, LASTNAME, USER_ID, EMAIL, PHONENUMBER, PASSWORD,dob) VALUES ('" + name + "', '" + lastn + "', '" + id + "', '" + useremail + "', '" + number + "', '" + pass + "',TO_DATE('" + birthdate + "','DD-MM-YYYY'))";
            ResultSet rs1 = s.grewal(sql1);
            rs1.close();
            String sql = "select * from USER_INFO where user_id='"+id+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                String USER_ID=rs.getString("USER_ID");
                String FIRSTNAME=rs.getString("FIRSTNAME");
                 String LASTNAME=rs.getString("LASTNAME");
                 String EMAIL=rs.getString("EMAIL");
                 Date DOB = rs.getDate("DOB");
                String newDate = dateFormatter(DOB);
                 int PHONENUMBER=rs.getInt("PHONENUMBER");
                 String PASSWORD=rs.getString("PASSWORD");
                 String USER_ROLE=rs.getString("USER_ROLE");
                 String ACTIVE=rs.getString("ACTIVE");
                        
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("LastName", LASTNAME);
                obj.accumulate("Email", EMAIL);
                obj.accumulate("PhoneNumber", PHONENUMBER);
                obj.accumulate("Dob",newDate);
                obj.accumulate("Password", PASSWORD);
                obj.accumulate("Role", USER_ROLE);
                obj.accumulate("Active", ACTIVE);

            } else {
                status = "wrong";

                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Message", "no column or row found");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "Eroor");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("UserName", id);
            obj.accumulate("Message",e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");

        return obj.toString();
    }

    @Path("login&{Uid}&{Upass}")
    @GET
    @Produces("application/json")
    public String login(@PathParam("Uid") String id, @PathParam("Upass") String pass) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from USER_INFO where user_id='" + id + "' and password='" + pass + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String FIRSTNAME = rs.getString("FIRSTNAME");
                String LASTNAME = rs.getString("LASTNAME");
                String USER_ID = rs.getString("USER_ID");
                String EMAIL = rs.getString("EMAIL");
                int PHONENUMBER = rs.getInt("PHONENUMBER");
                Date DOB = rs.getDate("DOB");
                String newDate = dateFormatter(DOB);
                String USER_ROLE=rs.getString("USER_ROLE");
                 String ACTIVE=rs.getString("ACTIVE");
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("LastName", LASTNAME);
                obj.accumulate("Email", EMAIL);
                obj.accumulate("PhoneNumber", PHONENUMBER);
                obj.accumulate("Dob",newDate );
                obj.accumulate("Password", pass);
                 obj.accumulate("Role", USER_ROLE);
                obj.accumulate("Active", ACTIVE);

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Message", "login failed:-wrong username or password ");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        }catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("UserName", id);
           obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());


        }
        System.out.println("Goodbye!");

        return obj.toString();
    }

    @Path("forgotpasswordemail&{Uid}&{Uemail}")
    @GET
    @Produces("application/json")
    public String forgotpasswordemail(@PathParam("Uid") String id, @PathParam("Uemail") String useremail) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from USER_INFO where user_id='" + id + "' and email='" + useremail + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String PASSWORD = rs.getString("PASSWORD");
                 String USER_ID = rs.getString("USER_ID");
                String EMAIL = rs.getString("EMAIL");
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("EMAIL", EMAIL);
                obj.accumulate("Password", PASSWORD);

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Email", useremail);
                obj.accumulate("Message", "no column or row found");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        }catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("UserName", id);
            obj.accumulate("Email", useremail);
              obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());


        }
        System.out.println("Goodbye!");

        return obj.toString();
    }

    @Path("forgotpasswordphone&{Uid}&{phonenumber}")
    @GET
    @Produces("application/json")
    public String forgotpasswordphone(@PathParam("Uid") String id, @PathParam("phonenumber") String number) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from USER_INFO where user_id='" + id + "' and PHONENUMBER='" + number + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String PASSWORD = rs.getString("PASSWORD");
                String USER_ID = rs.getString("USER_ID");
                int PHONENUMBER = rs.getInt("PHONENUMBER");
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("PhoneNumber", PHONENUMBER);
                obj.accumulate("Password", PASSWORD);

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("PhoneNumber", number);
                obj.accumulate("Message", "no column or row found");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("UserName", id);
            obj.accumulate("PhoneNumber", number);
            obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());


        }
        System.out.println("Goodbye!");

        return obj.toString();
    }
    
     @Path("changepassword&{Uid}&{password}")
    @GET
    @Produces("application/json")
    public String Changepassword(@PathParam("Uid") String userid,@PathParam("password") String pass) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql1 = "UPDATE user_info SET password = '"+pass+"' WHERE user_id = '"+userid+"'";
            ResultSet rs1 = s.grewal(sql1);
            rs1.close();
           
          String sql = "select * from USER_INFO where user_id='" + userid + "'and password='"+pass+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String FIRSTNAME = rs.getString("FIRSTNAME");
                String LASTNAME = rs.getString("LASTNAME");
                String USER_ID = rs.getString("USER_ID");
                String EMAIL = rs.getString("EMAIL");
                int PHONENUMBER = rs.getInt("PHONENUMBER");
                Date DOB = rs.getDate("DOB");
                String newDate = dateFormatter(DOB);
                String USER_ROLE=rs.getString("USER_ROLE");
                 String ACTIVE=rs.getString("ACTIVE");
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("LastName", LASTNAME);
                obj.accumulate("Email", EMAIL);
                obj.accumulate("PhoneNumber", PHONENUMBER);
                obj.accumulate("Dob",newDate );
                obj.accumulate("Password", pass);
                 obj.accumulate("Role", USER_ROLE);
                obj.accumulate("Active", ACTIVE);

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", userid);
                obj.accumulate("Password", pass);
                obj.accumulate("Message", "no column or row found");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
             obj.accumulate("UserName", userid);
                obj.accumulate("Password", pass);
            obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());


        }
        System.out.println("Goodbye!");

        return obj.toString();
    }
    @Path("viewallcategories")
    @GET
    @Produces("application/json")
    public String viewallcategory() throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();
            String sql = "select * from CATEGORIES where active='TRUE'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                rs.beforeFirst();
                JSONArray ary = new JSONArray();
                while (rs.next()) {
                    String CategoryId = rs.getString("CAT_ID");
                    String DESCRIPTION = rs.getString("CAT_DESCRIPTION");
                    String cat_name = rs.getString("CAT_NAME");
                    String SUBCAT_ID = rs.getString("SUBCAT_ID");
                    JSONObject newobj = new JSONObject();
                    newobj.accumulate("CategoryName", cat_name);
                    newobj.accumulate("CategoryId", CategoryId);
                    newobj.accumulate("DESCRIPTION", DESCRIPTION);
                    newobj.accumulate("SubCategoryId", SUBCAT_ID);
                    ary.add(newobj);
                    newobj.clear();
                }
                obj.accumulate("Categories", ary);
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Message", "no column or row found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
               obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
@Path("viewcategory&{name}")
    @GET
    @Produces("application/json")
    public String viewcategory(@PathParam("name") String cat_name) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select CATEGORIES.CAT_ID,CATEGORIES.SUBCAT_ID,CATEGORIES.CAT_NAME,CATEGORIES.ACTIVE,CATEGORIES.CAT_DESCRIPTION,games.GAME_DESCRIPTION,games.game_name,games.game_id from categories right join game_category on game_category.CAT_ID=categories.CAT_ID right join games on games.GAME_ID=game_category.GAME_ID where active ='TRUE' and CAT_NAME='" + cat_name + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String CategoryId = rs.getString("CAT_ID");
                String subcat_id = rs.getString("SUBCAT_ID");
                String active = rs.getString("ACTIVE");
                String CAT_DESCRIPTION = rs.getString("CAT_DESCRIPTION");
                obj.accumulate("CategoryName", cat_name);
                obj.accumulate("CategoryId", CategoryId);
                obj.accumulate("SubCategoryId", subcat_id);
                obj.accumulate("Active", active);
                obj.accumulate("CategoryDescription", CAT_DESCRIPTION);
                rs.beforeFirst();
                JSONArray ary = new JSONArray();
                while (rs.next()) {
                    String DESCRIPTION = rs.getString("GAME_DESCRIPTION");
                    String GAME_NAME = rs.getString("GAME_NAME");
                    String GAME_ID = rs.getString("GAME_ID");
                    JSONObject newobj = new JSONObject();
                    newobj.accumulate("GameDescription", DESCRIPTION);
                    newobj.accumulate("GameName", GAME_NAME);
                    newobj.accumulate("GameId", GAME_ID);
                    ary.add(newobj);
                    newobj.clear();
                }
                obj.accumulate("Games", ary);
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("CategoryName", cat_name);
                obj.accumulate("Message", "no column or row found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("TutorialName", cat_name);
            obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
    @Path("viewgame&{name}")
    @GET
    @Produces("application/json")
    public String viewgame(@PathParam("name") String name) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select games.GAME_DESCRIPTION,games.GAME_ID,games.GAME_NAME,games.ACTIVE,GAMESTATIONS.gs_id,GAMESTATIONS.loc_id,GAMESTATIONS.gs_name from games right join GAME_GAMESTAION on GAME_GAMESTAION.GAME_ID=games.GAME_ID right join GAMESTATIONS on GAME_GAMESTAION.GS_ID=GAMESTATIONS.GS_ID where active ='TRUE' and games.game_name='" + name + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String GAME_ID = rs.getString("GAME_ID");
                String ACTIVE = rs.getString("ACTIVE");
                String GAME_DESCRIPTION = rs.getString("GAME_DESCRIPTION");
                obj.accumulate("GameId", GAME_ID);
                obj.accumulate("GameName", name);
                obj.accumulate("Active", ACTIVE);
                obj.accumulate("GameDescription", GAME_DESCRIPTION);
                rs.beforeFirst();
                JSONArray ary = new JSONArray();
                while (rs.next()) {
                    String GS_ID = rs.getString("GS_ID");
                    String LOC_ID = rs.getString("LOC_ID");
                    String GS_NAME = rs.getString("GS_NAME");
                    JSONObject newobj = new JSONObject();
                    newobj.accumulate("GameStationId", GS_ID);
                    newobj.accumulate("GameStation", GS_NAME);
                    newobj.accumulate("LocationId", LOC_ID);
                    ary.add(newobj);
                    newobj.clear();
                }
                obj.accumulate("GameStations", ary);
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("GameName", name);
                obj.accumulate("Message", "no column or row found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("GameName", name);
            obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }

    @Path("viewgamestation&{name}")
    @GET
    @Produces("application/json")
    public String viewgamestation(@PathParam("name") String name) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select GAMESTATIONS.gs_id,GAMESTATIONS.loc_id,GAMESTATIONS.gs_name,LOCATIONS.LOC_NAME, LOCATIONS.CITY,LOCATIONS.POSTALCODE,LOCATIONS.BUILDING_NUMBER,LOCATIONS.COUNTRY,gamestations.ACTIVE from GAMESTATIONS right join LOCATIONS on LOCATIONS.LOC_ID=GAMESTATIONS.LOC_ID  where GAMESTATIONS.active ='TRUE' and GAMESTATIONS.active ='TRUE'  and GAMESTATIONS.GS_NAME='" + name + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String GS_ID = rs.getString("GS_ID");
                    obj.accumulate("GameStationId", GS_ID);
                    obj.accumulate("GameStation", name);
                    String LOC_NAME = rs.getString("LOC_NAME");
                    String LOC_ID = rs.getString("LOC_ID");
                    String CITY = rs.getString("CITY");
                    String POSTALCODE = rs.getString("POSTALCODE");
                    String BUILDING_NUMBER = rs.getString("BUILDING_NUMBER");
                    String COUNTRY = rs.getString("COUNTRY");
                    obj.accumulate("LocationId", LOC_ID);
                    obj.accumulate("LocationName", LOC_NAME);
                    obj.accumulate("City", CITY);
                    obj.accumulate("Postalcode", POSTALCODE);
                    obj.accumulate("BuildingNumber", BUILDING_NUMBER);
                    obj.accumulate("Country", COUNTRY);
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("GameStation", name);
                obj.accumulate("Message", "no column or row found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("GameStation", name);
            obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
 @Path("viewlocation&{name}")
    @GET
    @Produces("application/json")
    public String viewlocation(@PathParam("name") String name) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from locations where loc_name='" + name + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                    String LOC_ID = rs.getString("LOC_ID");
                    String CITY = rs.getString("CITY");
                    String POSTALCODE = rs.getString("POSTALCODE");
                    String BUILDING_NUMBER = rs.getString("BUILDING_NUMBER");
                    String COUNTRY = rs.getString("COUNTRY");
                    obj.accumulate("LocationId", LOC_ID);
                    obj.accumulate("LocationName", name);
                    obj.accumulate("City", CITY);
                    obj.accumulate("Postalcode", POSTALCODE);
                    obj.accumulate("BuildingNumber", BUILDING_NUMBER);
                    obj.accumulate("Country", COUNTRY);
                   
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("LocationName", name);
                obj.accumulate("Message", "no column or row found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("GameStation", name);
            obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
    public static Long timestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }
  public static String dateFormatter(Date d) {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        String dat = df.format(d);
        return dat;
    }
}
