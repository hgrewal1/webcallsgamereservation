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
                 long PHONENUMBER=rs.getLong("PHONENUMBER");
                 String PASSWORD=rs.getString("PASSWORD");
                 String USER_ROLE=rs.getString("USER_ROLE");
                 String ACTIVE=rs.getString("U_ACTIVE");
                        
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("LastName", LASTNAME);
                obj.accumulate("Email", EMAIL);
                obj.accumulate("PhoneNumber", PHONENUMBER);
                obj.accumulate("Dob",newDate);
                obj.accumulate("Password", PASSWORD);
                obj.accumulate("Role", USER_ROLE);
                obj.accumulate("UserActive", ACTIVE);

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

            String sql = "select * from USER_INFO where U_ACTIVE='TRUE' and user_id='" + id + "' and password='" + pass + "'";
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
                long PHONENUMBER = rs.getLong("PHONENUMBER");
                Date DOB = rs.getDate("DOB");
                String newDate = dateFormatter(DOB);
                String USER_ROLE=rs.getString("USER_ROLE");
                 String ACTIVE=rs.getString("U_ACTIVE");
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("LastName", LASTNAME);
                obj.accumulate("Email", EMAIL);
                obj.accumulate("PhoneNumber", PHONENUMBER);
                obj.accumulate("Dob",newDate );
                obj.accumulate("Password", pass);
                 obj.accumulate("Role", USER_ROLE);
                obj.accumulate("UserActive", ACTIVE);

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

    @Path("forgotpasswordemail&{Uemail}")
    @GET
    @Produces("application/json")
    public String forgotpasswordemail(@PathParam("Uemail") String useremail) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from USER_INFO where U_ACTIVE='TRUE' and email='" + useremail + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String PASSWORD = rs.getString("PASSWORD");
                 String USER_ID = rs.getString("USER_ID");
                String EMAIL = rs.getString("Email");
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("EMAIL", EMAIL);
                obj.accumulate("Password", PASSWORD);

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Email", useremail);
                obj.accumulate("Message", "no column or row found");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        }catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("Email", useremail);
              obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());


        }
        System.out.println("Goodbye!");

        return obj.toString();
    }

    @Path("forgotpasswordphone&{phonenumber}")
    @GET
    @Produces("application/json")
    public String forgotpasswordphone(@PathParam("phonenumber") String number) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from USER_INFO where U_ACTIVE='TRUE' and PHONENUMBER='" + number + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String PASSWORD = rs.getString("PASSWORD");
                String USER_ID = rs.getString("USER_ID");
                long PHONENUMBER = rs.getLong("PHONENUMBER");
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("PhoneNumber", PHONENUMBER);
                obj.accumulate("Password", PASSWORD);

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("PhoneNumber", number);
                obj.accumulate("Message", "no column or row found");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
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

            String sql1 = "UPDATE user_info SET password = '"+pass+"' WHERE U_ACTIVE='TRUE' and user_id = '"+userid+"'";
            ResultSet rs1 = s.grewal(sql1);
            rs1.close();
          
           
          String sql = "select * from USER_INFO where U_ACTIVE='TRUE' and user_id='" + userid + "'and password='"+pass+"'";
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
                long PHONENUMBER = rs.getLong("PHONENUMBER");
                Date DOB = rs.getDate("DOB");
                String newDate = dateFormatter(DOB);
                String USER_ROLE=rs.getString("USER_ROLE");
                 String ACTIVE=rs.getString("U_ACTIVE");
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("LastName", LASTNAME);
                obj.accumulate("Email", EMAIL);
                obj.accumulate("PhoneNumber", PHONENUMBER);
                obj.accumulate("Dob",newDate );
                obj.accumulate("Password", pass);
                 obj.accumulate("Role", USER_ROLE);
                obj.accumulate("UserActive", ACTIVE);

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
            String sql = "select * from CATEGORIES where C_ACTIVE='TRUE'";
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

            String sql = "select CATEGORIES.CAT_ID,CATEGORIES.SUBCAT_ID,CATEGORIES.CAT_NAME,CATEGORIES.C_ACTIVE,CATEGORIES.CAT_DESCRIPTION,games.GAME_DESCRIPTION,games.game_name,games.game_id,games.G_active from categories right join game_category on game_category.CAT_ID=categories.CAT_ID right join games on games.GAME_ID=game_category.GAME_ID where C_ACTIVE ='TRUE' and CAT_NAME='" + cat_name + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String CategoryId = rs.getString("CAT_ID");
                String subcat_id = rs.getString("SUBCAT_ID");
                String active = rs.getString("C_ACTIVE");
                String CAT_DESCRIPTION = rs.getString("CAT_DESCRIPTION");
                obj.accumulate("CategoryName", cat_name);
                obj.accumulate("CategoryId", CategoryId);
                obj.accumulate("SubCategoryId", subcat_id);
                obj.accumulate("CategoryActive", active);
                obj.accumulate("CategoryDescription", CAT_DESCRIPTION);
                rs.beforeFirst();
                JSONArray ary = new JSONArray();
                while (rs.next()) {
                    String DESCRIPTION = rs.getString("GAME_DESCRIPTION");
                    String GAME_NAME = rs.getString("GAME_NAME");
                    String GAME_ID = rs.getString("GAME_ID");
                    String G_ACTIVE = rs.getString("G_ACTIVE");
                    JSONObject newobj = new JSONObject();
                    newobj.accumulate("GameDescription", DESCRIPTION);
                    newobj.accumulate("GameName", GAME_NAME);
                    newobj.accumulate("GameId", GAME_ID);
                    newobj.accumulate("GameActive", G_ACTIVE);
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

            String sql = "select games.GAME_DESCRIPTION,games.GAME_ID,games.GAME_NAME,games.G_ACTIVE,GAMESTATIONS.gs_id,GAMESTATIONS.loc_id,GAMESTATIONS.gs_name,GAMESTATIONS.GS_ACTIVE,locations.loc_name ,locations.L_ACTIVE from games right join GAME_GAMESTAION on GAME_GAMESTAION.GAME_ID=games.GAME_ID right join GAMESTATIONS on GAME_GAMESTAION.GS_ID=GAMESTATIONS.GS_ID right join locations on locations.loc_id=gamestations.LOC_ID where games.G_active ='TRUE' and games.game_name='"+name+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String GAME_ID = rs.getString("GAME_ID");
                String G_ACTIVE = rs.getString("G_ACTIVE");
                String GAME_DESCRIPTION = rs.getString("GAME_DESCRIPTION");
                obj.accumulate("GameId", GAME_ID);
                obj.accumulate("GameName", name);
                obj.accumulate("GameActive", G_ACTIVE);
                obj.accumulate("GameDescription", GAME_DESCRIPTION);
                rs.beforeFirst();
                JSONArray ary = new JSONArray();
                while (rs.next()) {
                    String GS_ID = rs.getString("GS_ID");
                    String LOC_ID = rs.getString("LOC_ID");
                    String GS_ACTIVE = rs.getString("GS_ACTIVE");
                    String L_ACTIVE = rs.getString("L_ACTIVE");
                    String LOC_NAME = rs.getString("LOC_NAME");
                    String GS_NAME = rs.getString("GS_NAME");
                    JSONObject newobj = new JSONObject();
                    newobj.accumulate("GameStationId", GS_ID);
                    newobj.accumulate("GameStation", GS_NAME);
                    newobj.accumulate("GameStationActive", GS_ACTIVE);
                    newobj.accumulate("LocationActive", L_ACTIVE);
                    newobj.accumulate("LocationId", LOC_ID);
                    newobj.accumulate("LocationName", LOC_NAME);
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

            String sql = "select GAMESTATIONS.gs_id,GAMESTATIONS.loc_id,GAMESTATIONS.gs_name,LOCATIONS.LOC_NAME, LOCATIONS.CITY,LOCATIONS.POSTALCODE,LOCATIONS.BUILDING_NUMBER,LOCATIONS.COUNTRY,gamestations.GS_ACTIVE,locations.l_active from GAMESTATIONS right join LOCATIONS on LOCATIONS.LOC_ID=GAMESTATIONS.LOC_ID  where GAMESTATIONS.gs_active ='TRUE'  and GAMESTATIONS.GS_NAME='" + name + "'";
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
                    String GS_ACTIVE = rs.getString("GS_ACTIVE");
                    String L_ACTIVE = rs.getString("L_ACTIVE");
                    String CITY = rs.getString("CITY");
                    String POSTALCODE = rs.getString("POSTALCODE");
                    String BUILDING_NUMBER = rs.getString("BUILDING_NUMBER");
                    String COUNTRY = rs.getString("COUNTRY");
                    obj.accumulate("GameStationActive", GS_ACTIVE);
                    obj.accumulate("LocationId", LOC_ID);
                     obj.accumulate("LocationActive", L_ACTIVE);
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
 @Path("viewlocations")
    @GET
    @Produces("application/json")
    public String viewlocation() throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from locations where l_active ='TRUE'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                JSONArray ary=new JSONArray();
                rs.beforeFirst();
                while(rs.next()){
                    JSONObject obj1=new JSONObject();
                    String LOC_ID = rs.getString("LOC_ID");
                    String L_ACTIVE = rs.getString("L_ACTIVE");
                    String LOC_NAME = rs.getString("LOC_NAME");
                    String CITY = rs.getString("CITY");                    
                    String POSTALCODE = rs.getString("POSTALCODE");
                    String BUILDING_NUMBER = rs.getString("BUILDING_NUMBER");
                    String COUNTRY = rs.getString("COUNTRY");
                    obj1.accumulate("LocationId", LOC_ID);
                    obj1.accumulate("LocationActive", L_ACTIVE);
                    obj1.accumulate("LocationName", LOC_NAME);
                    obj1.accumulate("City", CITY);
                    obj1.accumulate("Postalcode", POSTALCODE);
                    obj1.accumulate("BuildingNumber", BUILDING_NUMBER);
                    obj1.accumulate("Country", COUNTRY);
                    ary.add(obj1);
                    obj1.clear();
                }
                obj.accumulate("Locations", ary);
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
    @Path("viewlocationgamestation&{name}")
    @GET
    @Produces("application/json")
    public String viewlocationgamestation(@PathParam("name") String name) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select locations.LOC_ID,locations.LOC_NAME,locations.CITY,locations.POSTALCODE,locations.BUILDING_NUMBER,locations.COUNTRY,GAMESTATIONS.GS_NAME from GAMESTATIONS right join LOCATIONS on LOCATIONS.loc_id=gamestations.LOC_ID where gamestations.GS_ACTIVE='TRUE' and gamestations.GS_NAME='" + name + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                    String LOC_ID = rs.getString("LOC_ID");
                    String CITY = rs.getString("CITY");
                     String LOC_NAME = rs.getString("LOC_NAME");
                    String POSTALCODE = rs.getString("POSTALCODE");
                    String BUILDING_NUMBER = rs.getString("BUILDING_NUMBER");
                    String COUNTRY = rs.getString("COUNTRY");
                     obj.accumulate("GameSation", name);
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
                obj.accumulate("GameSation", name);
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

            String sql = "select * from locations where l_active='TRUE' and loc_name='" + name + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                    String LOC_ID = rs.getString("LOC_ID");
                    String CITY = rs.getString("CITY");
                    String LOC_NAME = rs.getString("LOC_NAME");
                    String POSTALCODE = rs.getString("POSTALCODE");
                    String BUILDING_NUMBER = rs.getString("BUILDING_NUMBER");
                    String COUNTRY = rs.getString("COUNTRY");
                    String L_ACTIVE = rs.getString("L_ACTIVE");
                    obj.accumulate("LocationId", LOC_ID);
                    obj.accumulate("LocationName", LOC_NAME);
                    obj.accumulate("City", CITY);
                    obj.accumulate("Postalcode", POSTALCODE);
                    obj.accumulate("BuildingNumber", BUILDING_NUMBER);
                    obj.accumulate("Country", COUNTRY);
                    obj.accumulate("LocationActive", L_ACTIVE);
                   
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("GameSation", name);
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
     @Path("updateprofile&{Uname}&{Ulastn}&{Uemail}&{Unumber}&{Uid}&{Upass}")
    @GET
    @Produces("application/json")
    public String updateprofile(@PathParam("Uname") String name,@PathParam("Uid") String id,@PathParam("Upass") String pass, @PathParam("Ulastn") String lastn,  @PathParam("Uemail") String email, @PathParam("Unumber") String number,@PathParam("datebirth") String birthdate) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();
            String sql1 = "update USER_INFO set firstname='" + name + "',lastname= '" + lastn + "',email='"+email+" ',phonenumber= '" + number + "' where u_active='TRUE' and user_id='"+id+"' and password='"+pass+"'";
            ResultSet rs1 = s.grewal(sql1);
            rs1.close();
            String sql = "select * from USER_INFO where u_active='TRUE' and user_id='"+id+"'";
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
                 long PHONENUMBER=rs.getLong("PHONENUMBER");
                 String PASSWORD=rs.getString("PASSWORD");
                 String USER_ROLE=rs.getString("USER_ROLE");
                 String ACTIVE=rs.getString("u_ACTIVE");
                        
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("LastName", LASTNAME);
                obj.accumulate("Email", EMAIL);
                obj.accumulate("PhoneNumber", PHONENUMBER);
                obj.accumulate("Dob",newDate);
                obj.accumulate("Password", PASSWORD);
                obj.accumulate("Role", USER_ROLE);
                obj.accumulate("UserActive", ACTIVE);

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
    @Path("viewreservations&{Uid}")
    @GET
    @Produces("application/json")
    public String viewreservation(@PathParam("Uid") String id) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select reservations.R_DATE ,reservations.USER_ID,reservations.GS_ID,reservations.TS_ID,reservations.R_ACTIVE, timeslots.start_time,timeslots.end_time,gamestations.gs_name,gamestations.GS_ACTIVE from reservations right join timeslots on reservations.TS_ID=timeslots.TS_ID right join gamestations on reservations.GS_ID=gamestations.GS_ID where reservations.R_ACTIVE='TRUE' and reservations.user_id='" + id + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String U_ID = rs.getString("USER_ID");
                obj.accumulate("UserName", U_ID);
                JSONArray ary=new JSONArray();
                while(rs.next()){
                    JSONObject obj1=new JSONObject();
                String name = rs.getString("GS_NAME");
                String START_TIME = rs.getString("START_TIME");
                String END_TIME = rs.getString("END_TIME");
                String GS_ID = rs.getString("GS_ID");
                 String R_DATE = rs.getString("R_DATE");
                String R_ACTIVE = rs.getString("R_ACTIVE");
                String GS_ACTIVE = rs.getString("GS_ACTIVE");
                obj1.accumulate("ReservationActive", R_ACTIVE);
                obj1.accumulate("ReservationDate", R_DATE);
                obj1.accumulate("GameStationActive", GS_ACTIVE);
                obj1.accumulate("GameStation", name);
                obj1.accumulate("StartTime", START_TIME);
                obj1.accumulate("EndTime", END_TIME);
                obj1.accumulate("GamestationId", GS_ID);
                ary.add(obj1);
                obj1.clear();
                }
                obj.accumulate("Reservations", ary);
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
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("UserName", id);
            obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
    @Path("newreservations&{Rdate}&{Uid}&{GSid}&{TSid}")
    @GET
    @Produces("application/json")
    public String newreservations(@PathParam("Rdate") String date,@PathParam("Uid") String id,@PathParam("GSid") int gsid,@PathParam("TSid") int tsid) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql1 = "INSERT INTO RESERVATIONS (R_DATE, USER_ID, GS_ID, TS_ID)  VALUES (TO_TIMESTAMP('"+date+"','DD-MM-yyyy HH24:MI:SS'), '"+id+"',"+ gsid+", "+tsid+")";
            ResultSet rs1 = s.grewal(sql1);
            rs1.close();
            String sql="select * from Reservations where R_active ='TRUE' and user_id='"+id+"' and R_date =TO_TIMESTAMP('"+date+"','DD-MM-yyyy HH24:MI:SS')";
            ResultSet rs=s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String USER_ID = rs.getString("USER_ID");
                String GS_ID = rs.getString("GS_ID");
                String R_DATE = rs.getString("R_DATE");
                String TS_ID = rs.getString("TS_ID");
                String R_active = rs.getString("R_active");
                obj.accumulate("UserName", USER_ID);
                obj.accumulate("TimeSlotID", TS_ID);
                obj.accumulate("ReservationDate", R_DATE);
                obj.accumulate("ReservationActive", R_active);
                obj.accumulate("GamestationId", GS_ID);
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
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("UserName", id);
            obj.accumulate("Message", "error occurred :-" +e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
    @Path("viewavailability&{Gname}")
    @GET
    @Produces("application/json")
    public String viewavailability(@PathParam("Gname") String name) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();
           
            String sql = "select AVAILABILTY.TS_ID,AVAILABILTY.GS_ID,AVAILABILTY.A_ACTIVE ,GAMESTATIONS.GS_NAME ,GAMESTATIONS.gs_active,TIMESLOTS.t_active,TIMESLOTS.START_TIME,TIMESLOTS.END_TIME from AVAILABILTY right join gamestations on AVAILABILTY.gs_id=gamestations.GS_ID right join TIMESLOTS on TIMESLOTS.TS_ID=AVAILABILTY.ts_id where AVAILABILTY.A_ACTIVE='TRUE' and  gamestations.GS_NAME='"+name+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String GS_ID = rs.getString("GS_ID");
                String GS_ACTIVE = rs.getString("GS_ACTIVE");
                String GS_NAME = rs.getString("GS_NAME");
                obj.accumulate("GameStation", GS_NAME);
                obj.accumulate("GameStationID", GS_ID);
                obj.accumulate("GameStationActive", GS_ACTIVE);
                JSONArray ary=new JSONArray();
                while(rs.next()){
                    JSONObject obj1=new JSONObject();
                String T_ACTIVE = rs.getString("T_ACTIVE");
                String START_TIME = rs.getString("START_TIME");
                String END_TIME = rs.getString("END_TIME");
                obj1.accumulate("StartTime", START_TIME);
                obj1.accumulate("EndTime", END_TIME);
                obj1.accumulate("TimeslotActive", T_ACTIVE);
                ary.add(obj1);
                obj1.clear();
                }
                obj.accumulate("Avilablities", ary);
            }else {
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
            obj.accumulate("Status", "Eroor");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("GameStation", name);
            obj.accumulate("Message",e.getLocalizedMessage());

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
  public static String timeFormatter(Date d) {
        DateFormat df = new SimpleDateFormat("hh-mm-ss Am", Locale.ENGLISH);
        String dat = df.format(d);
        return dat;
    }
}
