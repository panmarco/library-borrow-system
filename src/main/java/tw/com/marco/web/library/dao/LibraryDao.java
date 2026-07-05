package tw.com.marco.web.library.dao;

import java.util.List;

import tw.com.marco.core.entity.BorrowingRecord;
import tw.com.marco.core.entity.Inventory;

public interface LibraryDao {

	int insertBorrowingRecord(Integer userId, Integer inventoryId);

	int updateInventoryStatus(Integer inventoryId, String status);
	
	int updateReturnTime(Integer userId, Integer inventoryId);
	
	List<Inventory> selectAllInventory();

	List<BorrowingRecord> selectAllBorrowingRecords();
}