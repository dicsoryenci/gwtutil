package fr.mikrosimage.gwt.client;

import java.util.Collection;

import ru.ppsrk.gwt.client.ClientUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;

public class ResizableDataGrid<T> extends DataGrid<T> {
    Widget emptyTableWidget = getEmptyTableWidget();

    public interface GwtCssDataGridResources extends DataGrid.Resources {
        @Source({ Style.DEFAULT_CSS, "DataGrid.css" })
        Style dataGrid();
    }

    public static final GwtCssDataGridResources gwtCssDataGridResources = GWT.create(GwtCssDataGridResources.class);

    static {
        gwtCssDataGridResources.dataGrid().ensureInjected();
    }

    public class DataGridResizableHeader extends ResizableHeader<T> {
        public DataGridResizableHeader(String title, Column<T, ?> column) {
            super(title, ResizableDataGrid.this, column);
        }

        @Override
        protected int getTableBodyHeight() {
            return ResizableDataGrid.this.getTableBodyElement().getOffsetHeight();
        }

        /*
         * @Override public void columnResized(int newWidth) {
         * super.columnResized(newWidth); refreshColumnWidths(); }
         */

    }

    public ResizableDataGrid(int pageSize, Resources resources) {
        super(pageSize, resources);
        setStyleName("border-bs");
    }

    public ResizableDataGrid() {
        super();
        setStyleName("border-bs");
    }

    public ScrollPanel getScrollPanel() {
        final HeaderPanel headerPanel = (HeaderPanel) getWidget();
        return (ScrollPanel) headerPanel.getContentWidget();
    }

    @Override
    public void addColumn(Column<T, ?> col, String headerString) {
        super.addColumn(col, new DataGridResizableHeader(headerString, col));
    }

    public void scrollToElement(T element) {
        getRowElement(getVisibleItems().indexOf(element)).getCells().getItem(0).scrollIntoView();
    }

    public void setup(AbstractDataProvider<T> dataProvider, SelectionModel<? super T> selectionModel, SimplePager simplePager,
            ListHandler<T> sortHandler) {
        if (dataProvider != null) {
            dataProvider.addDataDisplay(this);
        }
        if (selectionModel != null) {
            setSelectionModel(selectionModel);
        }
        if (simplePager != null) {
            simplePager.setDisplay(this);
        }
        if (sortHandler != null) {
            addColumnSortHandler(sortHandler);
        }
    }

    public void setLoadingData(ListDataProvider<T> dataProvider, Collection<? extends T> data) {
        if (data == null) {
            setEmptyTableWidget(getLoadingIndicator());
            dataProvider.getList().clear();
        } else {
            setEmptyTableWidget(emptyTableWidget);
            ClientUtils.replaceListDataProviderContents(dataProvider, data);
        }
    }

}
