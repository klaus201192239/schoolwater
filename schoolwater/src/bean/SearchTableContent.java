package bean;

public class SearchTableContent {
	private String tableId; //�������ID
	private String lastContentId;//���һ��TableContentInfo��ID
	private String [] columnNames;//�ֶ�ͷ
	private int limit;//��ѯ����
		
	public SearchTableContent() {
		super();
	}
	public SearchTableContent(String tableId, String lastContentId,
			String[] columnNames, int limit) {
		super();
		this.tableId = tableId;
		this.lastContentId = lastContentId;
		this.columnNames = columnNames;
		this.limit = limit;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getLastContentId() {
		return lastContentId;
	}
	public void setLastContentId(String lastContentId) {
		this.lastContentId = lastContentId;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
