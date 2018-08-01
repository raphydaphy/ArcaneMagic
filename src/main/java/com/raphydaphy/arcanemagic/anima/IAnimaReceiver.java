package com.raphydaphy.arcanemagic.anima;

public interface IAnimaReceiver extends IAnimaBlock
{
    public boolean isFull();

    public boolean receiveAnima(int anima, AnimaReceiveMethod method);
}