package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.toast.ProgressionUpdateToast;
import com.raphydaphy.crochet.network.IPacket;
import com.raphydaphy.crochet.network.MessageHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class ProgressionUpdateToastPacket implements IPacket {
    public static final Identifier ID = new Identifier(ArcaneMagic.DOMAIN, "progression_update_toast_packet");

    private boolean notebook;

    private ProgressionUpdateToastPacket() {

    }

    public ProgressionUpdateToastPacket(boolean notebook) {
        this.notebook = notebook;
    }

    @Override
    public void read(PacketByteBuf buf) {
        notebook = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(notebook);
    }

    @Override
    public Identifier getID() {
        return ID;
    }

    public static class Handler extends MessageHandler<ProgressionUpdateToastPacket> {
        @Override
        protected ProgressionUpdateToastPacket create() {
            return new ProgressionUpdateToastPacket();
        }

        @Override
        public void handle(PacketContext ctx, ProgressionUpdateToastPacket message) {
            MinecraftClient.getInstance().getToastManager().add(new ProgressionUpdateToast(message.notebook));
        }
    }
}
