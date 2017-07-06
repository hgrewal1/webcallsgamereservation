/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cegepgim;

import java.io.IOException;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;
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
    Long gsid,tsid,dayid;
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
            String sql = "select * from USER_INFO where user_id='"+id+"' and password='"+pass+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String FIRSTNAME=rs.getString("FIRSTNAME");
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("Message", "your account has been succsesfully created, please log in to your Account");

            } else {
                status = "wrong";

                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Message", "Username Must be Unique");

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
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("Message", "You have been succesfully logged in");

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Message", "Username or Password Incorrect");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        }catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("UserName", id);
           obj.accumulate("Message", e.getLocalizedMessage());


        }
        System.out.println("Goodbye!");

        return obj.toString();
    }

    @Path("forgotpasswordemail&{Uemail}&{userid}")
    @GET
    @Produces("application/json")
    public String forgotpasswordemail(@PathParam("Uemail") String useremail,@PathParam("userid") String id) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from USER_INFO where U_ACTIVE='TRUE' and email='" + useremail + "' and user_id='"+id+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                Email h=new Email();
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
                h.email(EMAIL, PASSWORD,USER_ID);
                obj.accumulate("Message", "Your password have been sent on your email account");

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Email", useremail);
                obj.accumulate("Message", "Email id or Username is wrong");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        }catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("Email", useremail);
              obj.accumulate("Message",e.getLocalizedMessage());


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
                obj.accumulate("Message", "Your password has been succesfully changed");

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", userid);
                obj.accumulate("Password", pass);
                obj.accumulate("Message", "Password you entered is not valid");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
             obj.accumulate("UserName", userid);
                obj.accumulate("Password", pass);
            obj.accumulate("Message", e.getLocalizedMessage());


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
                obj.accumulate("Message", "Category not found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
               obj.accumulate("Message",e.getLocalizedMessage());

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

          String sql = "select CATEGORIES.CAT_ID,CATEGORIES.SUBCAT_ID,CATEGORIES.CAT_NAME,CATEGORIES.CAT_DESCRIPTION,games.GAME_DESCRIPTION,games.game_name,games.game_id from categories right join game_category on game_category.CAT_ID=categories.CAT_ID right join games on games.GAME_ID=game_category.GAME_ID where C_ACTIVE ='TRUE' and CAT_NAME='" + cat_name + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String CategoryId = rs.getString("CAT_ID");
                String subcat_id = rs.getString("SUBCAT_ID");
                String CAT_DESCRIPTION = rs.getString("CAT_DESCRIPTION");
                obj.accumulate("CategoryName", cat_name);
                obj.accumulate("CategoryId", CategoryId);
                obj.accumulate("SubCategoryId", subcat_id);
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
                obj.accumulate("Message", "Category not found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("TutorialName", cat_name);
            obj.accumulate("Message",e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
    @Path("searchcategory&{name}")
    @GET
    @Produces("application/json")
    public String searchcategory(@PathParam("name") String catname) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

          String sql = "select CATEGORIES.CAT_ID,CATEGORIES.SUBCAT_ID,CATEGORIES.CAT_NAME,CATEGORIES.CAT_DESCRIPTION,games.GAME_DESCRIPTION,games.game_id ,GAMESTATIONS.gs_id,GAMESTATIONS.gs_name from categories right join game_category on game_category.CAT_ID=categories.CAT_ID right join games on games.GAME_ID=game_category.GAME_ID right join GAME_GAMESTAION on games.GAME_ID=GAME_GAMESTAION.GAME_ID right join GAMESTATIONS on GAMESTATIONS.gs_id=GAME_GAMESTAION.gs_id    where C_ACTIVE ='TRUE' and CATEGORIES.CAT_NAME='"+catname+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String CategoryId = rs.getString("CAT_ID");
                String subcat_id = rs.getString("SUBCAT_ID");
                String CAT_DESCRIPTION = rs.getString("CAT_DESCRIPTION");
                obj.accumulate("CategoryName", catname);
                obj.accumulate("CategoryId", CategoryId);
                obj.accumulate("SubCategoryId", subcat_id);
                obj.accumulate("CategoryDescription", CAT_DESCRIPTION);
                rs.beforeFirst();
                JSONArray ary = new JSONArray();
                while (rs.next()) {
                    String DESCRIPTION = rs.getString("GS_NAME");
                    String GAME_NAME = rs.getString("GS_ID");
                    
                    JSONObject newobj = new JSONObject();
                    newobj.accumulate("Gamestation", DESCRIPTION);
                    newobj.accumulate("GamestationId", GAME_NAME);
                    ary.add(newobj);
                    newobj.clear();
                }
                obj.accumulate("Gamestations", ary);
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("CategoryName", catname);
                obj.accumulate("Message", "Category not found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("TutorialName", catname);
            obj.accumulate("Message",e.getLocalizedMessage());

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

            String sql = "select games.GAME_DESCRIPTION,games.GAME_ID,games.GAME_NAME,GAMESTATIONS.gs_id,GAMESTATIONS.loc_id,GAMESTATIONS.gs_name,locations.loc_name  from games right join GAME_GAMESTAION on GAME_GAMESTAION.GAME_ID=games.GAME_ID right join GAMESTATIONS on GAME_GAMESTAION.GS_ID=GAMESTATIONS.GS_ID right join locations on locations.loc_id=gamestations.LOC_ID where games.G_active ='TRUE' and games.game_name='"+name+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String GAME_ID = rs.getString("GAME_ID");
                String GAME_DESCRIPTION = rs.getString("GAME_DESCRIPTION");
                obj.accumulate("GameId", GAME_ID);
                obj.accumulate("GameName", name);
                obj.accumulate("GameDescription", GAME_DESCRIPTION);
                rs.beforeFirst();
                JSONArray ary = new JSONArray();
                while (rs.next()) {
                    String GS_ID = rs.getString("GS_ID");
                    String LOC_ID = rs.getString("LOC_ID");
                    String LOC_NAME = rs.getString("LOC_NAME");
                    String GS_NAME = rs.getString("GS_NAME");
                    JSONObject newobj = new JSONObject();
                    newobj.accumulate("GameStationId", GS_ID);
                    newobj.accumulate("GameStation", GS_NAME);
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
                obj.accumulate("Message", "Game not found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("GameName", name);
            obj.accumulate("Message", e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
    @Path("viewallgames")
    @GET
    @Produces("application/json")
    public String viewallgames() throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from GAMES  where games.G_active ='TRUE' ";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                
                rs.beforeFirst();
                JSONArray ary = new JSONArray();
                while (rs.next()) {

                    JSONObject newobj = new JSONObject();
                    String GAME_ID = rs.getString("GAME_ID");
                    String GAME_NAME = rs.getString("GAME_NAME");
                    String GAME_DESCRIPTION = rs.getString("GAME_DESCRIPTION");
                    newobj.accumulate("GameId", GAME_ID);
                    newobj.accumulate("GameName", GAME_NAME);
                    newobj.accumulate("GameDescription", GAME_DESCRIPTION);
                    ary.add(newobj);
                    newobj.clear();
                }
                obj.accumulate("Games", ary);
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Message", "Game not found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("Message", e.getLocalizedMessage());

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

            String sql = "select GAMESTATIONS.gs_id,GAMESTATIONS.loc_id,GAMESTATIONS.gs_name,LOCATIONS.LOC_NAME, LOCATIONS.CITY,LOCATIONS.POSTALCODE,LOCATIONS.BUILDING_NUMBER,LOCATIONS.COUNTRY from GAMESTATIONS right join LOCATIONS on LOCATIONS.LOC_ID=GAMESTATIONS.LOC_ID  where GAMESTATIONS.gs_active ='TRUE'  and GAMESTATIONS.GS_NAME='" + name + "'";
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
                obj.accumulate("Message", "Gamestation not found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("GameStation", name);
            obj.accumulate("Message",e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
    @Path("viewallgamestation")
    @GET
    @Produces("application/json")
    public String viewallgamestation() throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();

            String sql = "select * from GAMESTATIONS";
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
                String GS_ID = rs.getString("GS_ID");
                String GS_NAME = rs.getString("GS_NAME");
                obj1.accumulate("GameStationId", GS_ID);
                obj1.accumulate("GameStation", GS_NAME);
                String LOC_ID = rs.getString("LOC_ID");
                obj1.accumulate("LocationId", LOC_ID);
                ary.add(obj1);
                obj1.clear();
                }
                obj.accumulate("GameStations", ary);
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Message", "Gamestation not found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("Message",e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
 @Path("viewalllocations")
    @GET
    @Produces("application/json")
    public String viewalllocations() throws SQLException, IOException {
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
                    String LOC_NAME = rs.getString("LOC_NAME");
                    String CITY = rs.getString("CITY");                    
                    String POSTALCODE = rs.getString("POSTALCODE");
                    String BUILDING_NUMBER = rs.getString("BUILDING_NUMBER");
                    String COUNTRY = rs.getString("COUNTRY");
                    obj1.accumulate("LocationId", LOC_ID);
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
                obj.accumulate("Message", "Location not found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());

            obj.accumulate("Message",e.getLocalizedMessage());

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

            String sql = "select * from locations right join gamestations on gamestations.loc_id=locations.Loc_id where l_active='TRUE' and loc_name='" + name + "'";
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
                    obj.accumulate("LocationId", LOC_ID);
                    obj.accumulate("LocationName", LOC_NAME);
                    obj.accumulate("City", CITY);
                    obj.accumulate("Postalcode", POSTALCODE);
                    obj.accumulate("BuildingNumber", BUILDING_NUMBER);
                    obj.accumulate("Country", COUNTRY);
                    JSONArray ary =new JSONArray ();
                    rs.beforeFirst();
                    while(rs.next()){
                        JSONObject obj1=new JSONObject();
                        String GS_NAME=rs.getString("GS_NAME");
                        obj1.accumulate("Gamestation", GS_NAME);
                        ary.add(obj1);
                        obj1.clear();
                        
                    }
                    obj.accumulate("Gamestations", ary);
                    
                   
            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("GameSation", name);
                obj.accumulate("Message", "Location not found");
            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("GameStation", name);
            obj.accumulate("Message",e.getLocalizedMessage());

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
            String sql1 = "update USER_INFO set firstname='" + name + "',lastname= '" + lastn + "',email='"+email+"',phonenumber= '" + number + "' where u_active='TRUE' and user_id='"+id+"' and password='"+pass+"'";
            ResultSet rs1 = s.grewal(sql1);
            rs1.close();
            String sql = "select * from USER_INFO where u_active='TRUE' and user_id='"+id+"' and password='"+pass+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Message", "Your profile have been updated");

            } else {
                status = "wrong";

                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Message", "Not A valid Email id");

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
    @Path("viewprofile&{Uid}&{Upass}")
    @GET
    @Produces("application/json")
    public String viewprofile(@PathParam("Uid") String id,@PathParam("Upass") String pass) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();
            String sql = "select * from USER_INFO where u_active='TRUE' and user_id='"+id+"' and password='"+pass+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String FIRSTNAME=rs.getString("FIRSTNAME");
                String LASTNAME=rs.getString("LASTNAME");
                String EMAIL=rs.getString("EMAIL");
                String PHONENUMBER=rs.getString("PHONENUMBER");
                obj.accumulate("FirstName", FIRSTNAME);
                obj.accumulate("LastName", LASTNAME);
                obj.accumulate("Email", EMAIL);
                obj.accumulate("PhoneNumber", PHONENUMBER);

            } else {
                status = "wrong";

                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Message", "Username not found");

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

            String sql = "select reservations.R_DATE ,reservations.USER_ID,WEEKDAYS.DAY_ID,WEEKDAYS.DAY_NAME, reservations.GS_ID,reservations.TS_ID,reservations.R_ACTIVE, timeslots.start_time,timeslots.end_time,gamestations.gs_name from reservations right join timeslots on reservations.TS_ID=timeslots.TS_ID right join gamestations on reservations.GS_ID=gamestations.GS_ID right join WEEKDAYS on WEEKDAYS.DAY_ID=reservations.DAY_ID  where reservations.R_ACTIVE='TRUE' and reservations.user_id='" + id + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String U_ID = rs.getString("USER_ID");
                obj.accumulate("UserName", U_ID);
                JSONArray ary=new JSONArray();
                rs.beforeFirst();
                while(rs.next()){
                    JSONObject obj1=new JSONObject();
                    String DAY_ID = rs.getString("DAY_ID");
                    String DAY_NAME = rs.getString("DAY_NAME");
                String name = rs.getString("GS_NAME");
                String START_TIME = rs.getString("START_TIME");
                String END_TIME = rs.getString("END_TIME");
                String GS_ID = rs.getString("GS_ID");
                 Date R_DATE = rs.getDate("R_DATE");
                obj1.accumulate("ReservationDate", dateFormatter(R_DATE));
                obj1.accumulate("GameStation", name);
                obj1.accumulate("StartTime", time(START_TIME));
                obj1.accumulate("EndTime", time(END_TIME));
                obj1.accumulate("DayId", DAY_ID);
                 obj1.accumulate("DAY_NAME", DAY_NAME);
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
                obj.accumulate("Message", "No reservation found");
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
    @Path("newreservations&{Rdate}&{Uid}&{GSname}&{TSstarttime}&{TSendtime}&{dayname}")
    @GET
    @Produces("application/json")
    
    public String newreservations(@PathParam("Rdate") String date,@PathParam("dayname") String dayname,@PathParam("Uid") String id,@PathParam("GSname") String name,@PathParam("TSstarttime") String starttime,@PathParam("TSendtime") String endtime) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();
            String sql2="select * from GAMESTATIONS where gs_active='TRUE' and GS_NAME='"+name+"'";
            ResultSet rs2=s.grewal(sql2);
            if(rs2.next())
            {
                 gsid=rs2.getLong("GS_ID");
            }
            rs2.close();
             String sql3="select * from TIMESLOTS where T_ACTIVE='TRUE' and START_TIME=TO_TIMESTAMP('"+starttime+"','HH24:MI') and end_time= TO_TIMESTAMP('"+endtime+"','HH24:MI')";
            ResultSet rs3=s.grewal(sql3);
            if(rs3.next())
            {
                 tsid=rs3.getLong("TS_ID");
            }
            rs3.close();
            String sq4="select day_id from weekdays where day_name='"+dayname+"'";
            ResultSet rs4=s.grewal(sq4);
            if(rs4.next()){
              dayid=rs4.getLong("DAY_ID");
            }
            rs4.close();
            String sql5="select * from Reservations where R_active ='TRUE' and R_date =TO_TIMESTAMP('"+date+"','DD-MM-yyyy HH24:MI:SS') and day_id="+dayid+" and gs_id="+gsid+" and ts_id="+tsid;
            ResultSet rs5=s.grewal(sql5);
           if(rs5.next()){
          status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Message", "Timeslot not available");
           rs5.close();}
           else{
            String sql1 = "INSERT INTO RESERVATIONS (R_DATE, USER_ID, GS_ID, TS_ID,day_id)  VALUES (TO_DATE('"+date+"','DD-MM-yyyy'), '"+id+"',"+ gsid+", "+tsid+","+dayid+")";
            ResultSet rs1 = s.grewal(sql1);
            rs1.close();
            String sql="select * from Reservations where R_active ='TRUE' and user_id='"+id+"' and R_date =TO_TIMESTAMP('"+date+"','DD-MM-yyyy HH24:MI:SS') and ts_id="+tsid+" and day_id="+dayid+" and gs_id="+gsid;
            ResultSet rs=s.grewal(sql);
           if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Message", "Gamestation has been reserved for you");
                 obj.accumulate("ReservationDate", date);
                 obj.accumulate("GameStation", name);
                 obj.accumulate("Username", id);
                 obj.accumulate("StartTime", starttime);
                 obj.accumulate("EndTime", endtime);
                 obj.accumulate("DayName", dayname);
            } else{
          status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Message", "Timeslot not available");}
            rs.close();
            s.closeConnection();
            s.closeStmt();
           
            }
            
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("UserName", id);
            obj.accumulate("Message",e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
    @Path("cancelreservations&{Rdate}&{Uid}&{GSname}&{TSstarttime}&{TSendtime}&{dayname}")
    @GET
    @Produces("application/json")
    public String cancelreservations(@PathParam("Rdate") String date,@PathParam("dayname") String dayname,@PathParam("Uid") String id,@PathParam("GSname") String name,@PathParam("TSstarttime") String starttime,@PathParam("TSendtime") String endtime) throws SQLException, IOException {
        try {
           create_connection s = new create_connection();
            s.getConnection();
            String sql2="select * from GAMESTATIONS where gs_active='TRUE' and GS_NAME='"+name+"'";
            ResultSet rs2=s.grewal(sql2);
            if(rs2.next())
            {
                 gsid=rs2.getLong("GS_ID");
            }
            rs2.close();
             String sql3="select * from TIMESLOTS where T_ACTIVE='TRUE' and START_TIME=TO_TIMESTAMP('"+starttime+"','HH24:MI') and end_time= TO_TIMESTAMP('"+endtime+"','HH24:MI')";
            ResultSet rs3=s.grewal(sql3);
            if(rs3.next())
            {
                
                 tsid=rs3.getLong("TS_ID");
            }
            rs3.close();
            String sq4="select day_id from weekdays where day_name='"+dayname+"'";
            ResultSet rs4=s.grewal(sq4);
            if(rs4.next()){
              dayid=rs4.getLong("DAY_ID");
            }
            rs4.close();

            String sql1 = "UPDATE RESERVATIONS SET R_ACTIVE = 'FALSE' WHERE R_date =TO_TIMESTAMP('"+date+"','DD-MM-yyyy HH24:MI:SS') and user_id = '"+id+"' and gs_id="+gsid+"and day_id="+dayid+"and ts_id="+tsid;
            ResultSet rs1 = s.grewal(sql1);
            rs1.close();
            String sql="select * from Reservations where R_active ='FALSE' and  R_date =TO_TIMESTAMP('"+date+"','DD-MM-yyyy HH24:MI:SS') and user_id = '"+id+"' and gs_id="+gsid+"and day_id="+dayid+"and ts_id="+tsid;
            ResultSet rs=s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Message", "Your reservation has been succesfully canceled");
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
            obj.accumulate("Message",e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");
        return obj.toString();
    }
    @Path("viewavailability&{GSname}&{TSstarttime}&{TSendtime}&{dayname}&{date}")
    @GET
    @Produces("application/json")
    
    public String viewavailability(@PathParam("GSname") String gsname,@PathParam("date") String date,@PathParam("dayname") String dayname,@PathParam("TSstarttime") String starttime,@PathParam("TSendtime") String endtime) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();
            String sql2="select * from GAMESTATIONS where gs_active='TRUE' and GS_NAME='"+gsname+"'";
            ResultSet rs2=s.grewal(sql2);
            if(rs2.next())
            {
                 gsid=rs2.getLong("GS_ID");
            }
            rs2.close();
             String sql3="select * from TIMESLOTS where T_ACTIVE='TRUE' and START_TIME=TO_TIMESTAMP('"+starttime+"','HH24:MI') and end_time= TO_TIMESTAMP('"+endtime+"','HH24:MI')";
            ResultSet rs3=s.grewal(sql3);
            if(rs3.next())
            {
                 tsid=rs3.getLong("TS_ID");
            }
            rs3.close();
            String sq4="select day_id from weekdays where day_name='"+dayname+"'";
            ResultSet rs4=s.grewal(sq4);
            if(rs4.next()){
              dayid=rs4.getLong("DAY_ID");
            }
            rs4.close();
           String sql5="select * from Reservations where R_active ='TRUE' and R_date =TO_TIMESTAMP('"+date+"','DD-MM-yyyy HH24:MI:SS') and day_id="+dayid+" and gs_id="+gsid+" and ts_id="+tsid;
            ResultSet rs5=s.grewal(sql5);
           if(rs5.next()){
          status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Message", "Gamestation is already reserved");
           rs5.close();
           }
           else{
            String sql = "select AVAILABILTY.TS_ID,AVAILABILTY.GS_ID,AVAILABILTY.A_ACTIVE ,GAMESTATIONS.GS_NAME ,TIMESLOTS.START_TIME,TIMESLOTS.END_TIME ,weekdays.day_name from AVAILABILTY right join gamestations on AVAILABILTY.gs_id=gamestations.GS_ID right join TIMESLOTS on TIMESLOTS.TS_ID=AVAILABILTY.ts_id right join WEEKDAYS on WEEKDAYS.day_id=AVAILABILTY.day_id where AVAILABILTY.A_ACTIVE='TRUE' and AVAILABILTY.TS_ID="+tsid+" and AVAILABILTY.day_id="+dayid+" and AVAILABILTY.GS_ID="+gsid;
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String GS_NAME = rs.getString("GS_NAME");
                obj.accumulate("GameStation", GS_NAME);
                
                String DAY_NAME = rs.getString("DAY_NAME");
                
                obj.accumulate("Day", DAY_NAME);
                obj.accumulate("StartTime", starttime);
                obj.accumulate("EndTime", endtime);
                obj.accumulate("Date", date);
                obj.accumulate("Message", "Gamestation is available");
                       
                
            }else {
                status = "wrong";

                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("GameStation", gsname);
                obj.accumulate("Message", "Timeslot  not available");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();
           }
        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "Eroor");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("GameStation", gsname);
            obj.accumulate("Message",e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");

        return obj.toString();
    }
     
     @Path("viewschedule&{GSname}&{Dayname}")
    @GET
    @Produces("application/json")
    
    public String viewschedule(@PathParam("GSname") String gsname,@PathParam("Dayname") String dayname) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();
           
            String sql = "select AVAILABILTY.TS_ID,AVAILABILTY.GS_ID,AVAILABILTY.A_ACTIVE ,GAMESTATIONS.GS_NAME ,TIMESLOTS.START_TIME,TIMESLOTS.END_TIME ,weekdays.day_name from AVAILABILTY right join gamestations on AVAILABILTY.gs_id=gamestations.GS_ID right join TIMESLOTS on TIMESLOTS.TS_ID=AVAILABILTY.ts_id right join WEEKDAYS on WEEKDAYS.day_id=AVAILABILTY.day_id where AVAILABILTY.A_ACTIVE='TRUE'   and gamestations.gs_name='"+gsname+"' and weekdays.day_name='"+dayname+"'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                String GS_NAME = rs.getString("GS_NAME");
                obj.accumulate("GameStation", GS_NAME);
                 String DAY_NAME = rs.getString("DAY_NAME");
                 obj.accumulate("Day", DAY_NAME);
                JSONArray ary=new JSONArray();
                rs.beforeFirst();
                while(rs.next()){
                    JSONObject obj1=new JSONObject();
                String START_TIME = rs.getString("START_TIME");
                String END_TIME = rs.getString("END_TIME");
                obj1.accumulate("StartTime", time(START_TIME));
                obj1.accumulate("EndTime", time(END_TIME));
                ary.add(obj1);
                obj1.clear();
                }
                obj.accumulate("TimeSlots", ary);
                
            }else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Message", "Schedule not found for Gamestation");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        } catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "Eroor");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("Message",e.getLocalizedMessage());

        }
        System.out.println("Goodbye!");

        return obj.toString();
    }
     
     @Path("deactivateaccount&{Uid}&{Upass}")
    @GET
    @Produces("application/json")
    public String deactivateaccount(@PathParam("Uid") String id, @PathParam("Upass") String pass) throws SQLException, IOException {
        try {
            create_connection s = new create_connection();
            s.getConnection();
             String sql2="update user_info set u_active='FALSE' where user_id='"+id+"' and password='" + pass + "'";
             ResultSet rs2=s.grewal(sql2);
             rs2.close();
            String sql = "select * from USER_INFO where U_ACTIVE='FALSE' and user_id='" + id + "' and password='" + pass + "'";
            ResultSet rs = s.grewal(sql);
            if (rs.next()) {
                status = "ok";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("Message", "Your account has been succesfully Deactivated");

            } else {
                status = "wrong";
                obj.accumulate("Status", status);
                Long timenow = timestamp();
                obj.accumulate("Timestmap", timenow);
                obj.accumulate("UserName", id);
                obj.accumulate("Message", "Password is incorrect");

            }
            rs.close();
            s.closeConnection();
            s.closeStmt();

        }catch (SQLException|ClassNotFoundException e) {
            obj.accumulate("Status", "ERROR");
            obj.accumulate("TimeStamp", timestamp());
            obj.accumulate("UserName", id);
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
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String dat = df.format(d);
        return dat;
    }
 public String date(String g){
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;
        try
        {
            date = form.parse(g);
        }
        catch (ParseException e)
        {

            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy");
        String newDateStr = postFormater.format(date);
        return newDateStr;
    }
   public String time(String g){
    SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    java.util.Date date = null;
    try
    {
        date = form.parse(g);
    }
    catch (ParseException e)
    {

        e.printStackTrace();
    }
    SimpleDateFormat postFormater = new SimpleDateFormat("HH:mm");
    String newDateStr = postFormater.format(date);
    return newDateStr;
}
}
