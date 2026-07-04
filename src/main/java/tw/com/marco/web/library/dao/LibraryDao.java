package tw.com.marco.web.library.dao;

import java.util.List;

import tw.com.marco.core.entity.BorrowingRecord;
import tw.com.marco.core.entity.Inventory;

public interface LibraryDao {

	List<Inventory> selectInventoryByStatus();

	int updateInventoryStatus(Integer inventoryId, String status);

	int insertBorrowingRecord(Integer userId, Integer inventoryId);

	List<BorrowingRecord> selectAllBorrowingRecords();
	
	int updateReturnTime(Integer userId, Integer inventoryId);
}