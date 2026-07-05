package tw.com.marco.web.library.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.marco.core.pojo.AllData;
import tw.com.marco.web.library.dao.LibraryDao;
import tw.com.marco.web.library.service.LibraryService;

@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {
	@Autowired
	private LibraryDao dao;

	@Override
	public AllData getAllData(Integer userId) {
		AllData data = new AllData();
		data.setInventories(dao.selectAllInventory());
		data.setRecords(dao.selectBorrowingRecordsByUserId(userId));
		data.setSuccessful(true); // false，前端按鈕渲染錯誤
		return data;
	}

	@Override
	public AllData borrowBook(Integer userId, Integer inventoryId) {
		AllData data = getAllData(userId);

		String status = dao.selectInventoryStatus(inventoryId);

		if ("出借中".equals(status)) {
			data.setSuccessful(false);
			data.setMessage("此書已被借閱");
			return data;
		}

		Integer update = dao.updateInventoryStatus(inventoryId, "出借中");
		Integer insert = dao.insertBorrowingRecord(userId, inventoryId);
		if (update < 1 || insert < 1) {
			data.setSuccessful(false);
			data.setMessage("系統異常:借閱失敗");
			return data;
		}
		data = getAllData(userId);
		data.setSuccessful(true);
		data.setMessage("借閱成功");
		return data;
	}

	@Override
	public AllData returnBook(Integer userId, Integer inventoryId) {
		AllData data = getAllData(userId);
		
		Integer borrowerId = dao.selectBorrowingRecordUserIdByInventoryId(inventoryId);
		
		if (!borrowerId.equals(userId)) {
			data.setSuccessful(false);
			data.setMessage("非本人借閱，無法歸還");
			return data;
		}
		
		Integer updateInventory = dao.updateInventoryStatus(inventoryId, "在庫");
		Integer updateReturnTime = dao.updateReturnTime(userId, inventoryId);
		if (updateInventory < 1 || updateReturnTime < 1) {
			data.setSuccessful(false);
			data.setMessage("系統異常:歸還失敗");
			return data;
		}
		data = getAllData(userId);
		data.setSuccessful(true);
		data.setMessage("歸還成功");
		return data;
	}
}
