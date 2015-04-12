package com.jnodes.tools.searchrobot.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Datas {

	private List<Data> list = new ArrayList<Data>();
	
	public void add(int index, String value){
		this.list.add(new Data(index, value));
	}
	
	public void sort(){
		Collections.sort(this.list);
	}
	
	public List<String> getDataList(){
		
		this.sort();
		
		List<String> dataList = new ArrayList<String>();
		for (Data data : this.list) {
			dataList.add(data.getValue());
		}
		return dataList;
	}
	
	private static class Data implements Comparable<Data> {
		private int index;
		private String value;

		public Data(int index, String value) {
			this.setIndex(index);
			this.setValue(value);
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public int compareTo(Data data) {
			if(this.index > data.getIndex()){
				return 1;
			}else if(this.index < data.getIndex()){
				return -1;
			}
			return 0;
		}

	}

}
