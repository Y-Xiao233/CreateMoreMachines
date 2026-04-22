package net.yxiao233.createmoremachines.api.registry;

public interface ICMMPlugin {
    default boolean shouldLoad(){
        return true;
    }
    default void registryRegistrate(){

    }
    default void registryTiers(){

    }
    default void registryPartialModels(){

    }

    default void onRegister(){

    }

    default void onPostRegister(){

    }
}
