package ru.ppsrk.gwt.client;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;

public class PopupMenuItem extends Label implements HasSelectionHandlers<PopupMenuItem> {
    private boolean enabled = true;
    private String value;
    public static final String MENU_ITEM_SELECTED = "menu-item-selected";
    public static final String MENU_ITEM_ENABLED = "menu-item-enabled";
    public static final String MENU_ITEM_DISABLED = "menu-item-disabled";

    {
        addStyleName("menu-item");
    }

    public PopupMenuItem(String title, SelectionHandler<PopupMenuItem> onSelectionHandler) {
        this(title);
        addSelectionHandler(onSelectionHandler);
    }

    public PopupMenuItem(String title) {
        super(title);
    }

    public PopupMenuItem(String title, boolean enabled) {
        this(title);
        this.enabled = enabled;
    }

    public PopupMenuItem(String title, String value) {
        this(title);
        this.value = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
        return result;
    }

    /**
     * use {@link #isValueEquals(String)} instead
     */
    @Override
    @Deprecated
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return obj.equals(value);
        }
        return super.equals(obj);
    }

    public boolean isValueEquals(String val) {
        return val.equals(value);
    }
    
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<PopupMenuItem> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

}
