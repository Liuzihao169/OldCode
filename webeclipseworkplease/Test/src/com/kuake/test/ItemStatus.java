package com.kuake.test;

public enum ItemStatus {
	 PACKING_NOT_USE(1),  // 未领用
	 PACKING_HAS_USED(2);  // 已领用
	private Integer useStatus;
	private ItemStatus(Integer useStatus) {
		         this.useStatus = useStatus;
		     }
	public Integer getUseStatus(){
		return useStatus;
	}
}
 