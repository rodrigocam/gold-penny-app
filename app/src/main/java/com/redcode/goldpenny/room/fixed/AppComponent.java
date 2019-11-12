package com.redcode.goldpenny.room.fixed;

import android.app.Application;

import com.redcode.goldpenny.room.editable.AppModule;
import com.redcode.goldpenny.room.editable.ActivityModule;
import com.redcode.goldpenny.App;
import com.redcode.goldpenny.room.editable.FragmentModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {ActivityModule.class, FragmentModule.class, AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);
}
