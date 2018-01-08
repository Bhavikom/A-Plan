package de.smacsoftwares.aplanapp.dagger2;

import javax.inject.Singleton;

import dagger.Component;
import de.smacsoftwares.aplanapp.FirebaseIDService;
import de.smacsoftwares.aplanapp.MyFirebaseMessagingService;
import de.smacsoftwares.aplanapp.activity.AdminSplashActivity;
import de.smacsoftwares.aplanapp.activity.AdminSplashActivityForMobile;
import de.smacsoftwares.aplanapp.activity.ForgottPasswordActivity;
import de.smacsoftwares.aplanapp.activity.GetUrlActivity;
import de.smacsoftwares.aplanapp.activity.GetUrlMobileActivity;
import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.activity.LoginActivity;
import de.smacsoftwares.aplanapp.activity.LoginMobileActivity;
import de.smacsoftwares.aplanapp.activity.SelectLoginType;
import de.smacsoftwares.aplanapp.activity.SplashActivity;
import de.smacsoftwares.aplanapp.activity.TutorialActivity;
import de.smacsoftwares.aplanapp.caldroid.CaldroidGridAdapter;
import de.smacsoftwares.aplanapp.fragment.FragmentChangePassword;
import de.smacsoftwares.aplanapp.fragment.FragmentDashBoard;
import de.smacsoftwares.aplanapp.fragment.FragmentDashBoardMobile;
import de.smacsoftwares.aplanapp.fragment.FragmentFilterDate;
import de.smacsoftwares.aplanapp.fragment.FragmentFilterFolder;
import de.smacsoftwares.aplanapp.fragment.FragmentFilterText;
import de.smacsoftwares.aplanapp.fragment.FragmentFilterUserDefined;
import de.smacsoftwares.aplanapp.fragment.FragmentProfile;
import de.smacsoftwares.aplanapp.fragment.FragmentProject;
import de.smacsoftwares.aplanapp.fragment.FragmentResource;
import de.smacsoftwares.aplanapp.fragment.FragmentSelectFilter;
import de.smacsoftwares.aplanapp.fragment.FragmentSetting;
import de.smacsoftwares.aplanapp.fragment.FragmentSetting2;
import de.smacsoftwares.aplanapp.fragment.FragmentSupport;
import de.smacsoftwares.aplanapp.fragment.FragmentSupportAbout;
import de.smacsoftwares.aplanapp.fragment.FragmentSupportContact;
import de.smacsoftwares.aplanapp.fragment.FragmentTable;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial1;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial2;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial3;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial4;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial5;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial6;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial7;
import de.smacsoftwares.aplanapp.fragment.FragmentTutorial8;
import de.smacsoftwares.aplanapp.util.GenericHelper;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    /*void inject(SplashActivity activity);
    void inject(AdminSplashActivity activity);
    void inject(AdminSplashActivityForMobile activity);
    void inject(SelectLoginType activity);
    void inject(GetUrlActivity activity);
    void inject(GetUrlMobileActivity activity);
    void inject(LoginMobileActivity activity);
    void inject(LoginActivity activity);
    void inject(HomeActivity activity);
    void inject(ForgottPasswordActivity activity);
    void inject(TutorialActivity activity);

    void inject(FragmentChangePassword fragment);
    void inject(FragmentDashBoard fragment);
    void inject(FragmentDashBoardMobile fragment);
    void inject(FragmentFilterDate fragment);
    void inject(FragmentFilterFolder fragment);
    void inject(FragmentFilterText fragment);
    void inject(FragmentFilterUserDefined fragment);
    void inject(FragmentProfile fragment);
    void inject(FragmentProject fragment);
    void inject(FragmentResource fragment);
    void inject(FragmentSelectFilter fragment);
    void inject(FragmentSetting fragment);

    void inject(FragmentSetting2 fragment);
    void inject(FragmentSupport fragment);
    void inject(FragmentSupportAbout fragment);
    void inject(FragmentSupportContact fragment);
    void inject(FragmentTable fragment);
    void inject(FragmentTutorial1 fragment);
    void inject(FragmentTutorial2 fragment);
    void inject(FragmentTutorial3 fragment);
    void inject(FragmentTutorial4 fragment);
    void inject(FragmentTutorial5 fragment);
    void inject(FragmentTutorial6 fragment);
    void inject(FragmentTutorial7 fragment);
    void inject(FragmentTutorial8 fragment);

    void inject(GenericHelper genericHelper);
    void inject(CaldroidGridAdapter adapter);
    void inject(FirebaseIDService service);
    void inject(MyFirebaseMessagingService service);*/
}
