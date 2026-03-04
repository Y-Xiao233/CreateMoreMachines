package net.yxiao233.createmoremachines.api.registry;


public interface ICMMPlugin {
    default void registryRegistrate(){

    }
    CMMRegistrate getRegistrate();
    default void registryTiers(){

    }
}
