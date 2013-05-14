package com.youxiachai.oschina.app.api;

public interface Api {
	
	public interface Query {
		public String uid = "uid";
		public String pageIndex = "pageIndex";
		public String pageSize = "pageSize";
		
		public String id = "id";
		public String catalog = "catalog";
		
	}
	
	public interface Key{
		public String dataState = "datastate";
	}
}
