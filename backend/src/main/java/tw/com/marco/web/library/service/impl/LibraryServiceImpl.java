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
	public AllData getAllData() {
		AllData data = new AllData();
		data.setInventories(dao.selectAllInventory());
		data.setRecords(dao.selectAllBorrowingRecords());
		return data;
	}

	@Override
	public AllData borrowBook(Integer userId, Integer inventoryId) {
		Integer update = dao.updateInventoryStatus(inventoryId, "已借閱");
		Integer insert = dao.insertBorrowingRecord(userId, inventoryId);
		AllData data = getAllData();
		if (update < 1 || insert < 1) {
			data.setSuccessful(false);
			data.setMessage("系統異常:借閱失敗");
			return data;
		}
		data.setSuccessful(true);
		data.setMessage("借閱成功");
		return data;
	}

	@Override
	public AllData returnBook(Integer userId, Integer inventoryId) {
		Integer updateInventory = dao.updateInventoryStatus(inventoryId, "在庫");
		Integer updateReturnTime = dao.updateReturnTime(userId, inventoryId);
		AllData data = getAllData();
		if (updateInventory < 1 || updateReturnTime < 1) {
			data.setSuccessful(false);
			data.setMessage("系統異常:歸還失敗");
			return data;
		}
		data.setSuccessful(true);
		data.setMessage("歸還成功");
		return data;
	}
}
