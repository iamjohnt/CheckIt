package com.alexhinds.checkit;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class SideMenu {

    public Menu menu;
    // final instances to hold id of the menu groups
    private final int yourListsID = R.id.your_lists;
    private final int sharedListID = R.id.shared_lists;

    public SideMenu(Menu menu){
        this.menu = menu;

    }

    public void populateMenu(String list, boolean sharedList){
        // adding menu items using a string,  mark item as checked
        menu.add(yourListsID,501,101,list);
        menu.add(sharedListID,1001,1001,list);
        MenuItem menuItem = menu.findItem(501);
        menuItem.setCheckable(true);
    }



}
