package de.smacsoftwares.aplanapp.util;

import java.util.ArrayList;

import de.smacsoftwares.aplanapp.DashboardModel.DashboardResponse;
import de.smacsoftwares.aplanapp.GetProfileModel.GetProfilePojo;
import de.smacsoftwares.aplanapp.GetUserProfileModel.GetUserProfileResponse;
import de.smacsoftwares.aplanapp.Model.ChangePassResponse;
import de.smacsoftwares.aplanapp.Model.ClientAndResponsiblePojo;
import de.smacsoftwares.aplanapp.Model.CreateProfilePojo;
import de.smacsoftwares.aplanapp.Model.DeleteProfilePojo;
import de.smacsoftwares.aplanapp.Model.FolderDataset;
import de.smacsoftwares.aplanapp.Model.ForgottResponse;
import de.smacsoftwares.aplanapp.Model.LoginPojo;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit2.http.FormUrlEncoded;


public interface InterfaceServiceClass
{

    /*Retrofit get annotation with our URL
       And our method that will return us the list ob GetterSetterClass
    */
    /*@Multipart
    @POST("/DemoUserDetail")
    public void callAdminSplash(@Part("Email") String email,
                          @Part("Name") String password,
                          @Part("Company") String database,
                          @Part("IsNewsletter") boolean serveruser,
                          @Part("IsDemoUser") boolean serverpasss,
                          Callback<AdminSplashModel> response);*/
    @Multipart
    @POST("/HostedUrl")
    public void callLogin(@Part("UserName") String email,
                          @Part("Password") String password,
                          Callback<LoginPojo> response);
    @Multipart
    @POST("/Authenticate")
    public void callAuthenticate(@Part("Email") String email,
                                 @Part("Password") String password,
                                 Callback<LoginPojo> response);

    @Multipart
    @POST("/Profiles")
    public void callGetProfile(@Part("UserId") String userId,
                               @Part("Lang") String lang,
                               Callback<GetProfilePojo> response);

    @Multipart
    @POST("/Profile")
    public void callCrateProfile(@Part("UserId") String userid,
                                 @Part("Name") String name,
                                 @Part("ColsDetail") String coldetail,
                                 @Part("ExpandedIds") String expandid,
                                 @Part("Lang") String lang,
                                 Callback<CreateProfilePojo> response);
    @Multipart
    @POST("/Profile")
    public void callDeleteProfile(@Part("UserId") String userid,
                                  @Part("Id") String id,
                                  Callback<DeleteProfilePojo> response);
    @Multipart
    @POST("/Profiles")
    public void callGetUserProfile(@Part("UserId") String userId, Callback<GetUserProfileResponse> response);

    @Multipart
    @POST("/ForgotPassword")
    public void callForgott(@Part("Email") String email, Callback<ForgottResponse> response);

    @Multipart
    @POST("/ChangePassword")
    public void callChangePass(@Part("Id") String id, @Part("OldPassword") String oldpass, @Part("NewPassword") String newpass, Callback<ChangePassResponse> response);

    @Multipart
    @POST("/Dashboard")
    public void callDashboard(@Part("UserId") String id, @Part("DateRange") String daterange, @Part("Psearch") String psearch,
                              @Part("Tsearch") String tsearch, @Part("POrder") String porder,
                              @Part("Limit") String limit, @Part("TOrder") String torder, Callback<DashboardResponse> response);

    @FormUrlEncoded
    @GET("/Get")
    public void callFolder(Callback<ArrayList<FolderDataset>> response);

    @FormUrlEncoded
    @POST("/ResponsiblePersonsAndClients")
    public void callClientResponsible(Callback<ClientAndResponsiblePojo> response);

}