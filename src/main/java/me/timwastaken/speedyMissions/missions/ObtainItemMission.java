package me.timwastaken.speedyMissions.missions;

import me.timwastaken.speedyMissions.GameManager;
import me.timwastaken.speedyMissions.Notifications;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObtainItemMission extends TickMission {
    private final Material itemMaterial;

    public ObtainItemMission(GameManager gameManager, Material itemMaterial) {
        super(gameManager);
        this.itemMaterial = itemMaterial;
    }

    @Override
    public void startMission() {
        this.startTask(() -> {
            List<Player> finishingPlayers = new ArrayList<>();
            for (Iterator<Player> it = this.gameManager.getActivePlayerIterator(); it.hasNext(); ) {
                Player p = it.next();
                if (!p.getInventory().contains(itemMaterial)) continue;
                finishingPlayers.add(p);
            }
            if (!finishingPlayers.isEmpty()) this.playerFinishCallback.accept(finishingPlayers);
        });
    }

    @Override
    public void stopMission() {
        super.stopMission();
    }

    @Override
    public String getMissionDescription() {
        return String.format("Obtain item: %s", Notifications.getItemName(this.itemMaterial));
    }

    @Override
    public long getGameTickDuration() {
        return 2 * GameManager.MINUTES;
    }

    @Override
    public BaseComponent[] getMissionChatAnnouncement() {
        TextComponent text = new TextComponent(
                Notifications.getChatAnnouncement(this.getMissionDescription())
        );

        TextComponent wikiLink = new TextComponent(
                ChatColor.GREEN + " [Wiki]"
        );
        String url = String.format(
                "https://www.google.com/search?q=%s+site%%3Aminecraft.wiki",
                this.itemMaterial.name().replaceAll("_", "+").toLowerCase()
        );
        wikiLink.setClickEvent(new ClickEvent(
                ClickEvent.Action.OPEN_URL,
                url
        ));
        wikiLink.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text(String.format(
                        "Search the minecraft wiki for '%s'",
                        Notifications.getItemName(this.itemMaterial)
                        ))
        ));
        return new BaseComponent[] {text, wikiLink};
    }
}
