const { ref, onMounted } = Vue;

// 強制跨域攜帶 Cookie 與 Session 連結
axios.defaults.withCredentials = true;

Vue.createApp({
    setup() {
        const API_BASE = 'http://localhost:8080/library-borrow-system';

        // 當前使用者資訊(暫時寫死')
        const currentUser = ref({
            userId: 1,
            userName: "marco"
        });

        // 庫存陣列變數
        const inventoryList = ref([]);

        // 借閱紀錄清單
        const borrowingRecords = ref([]);

        // 🌟 初始化：獲取所有在庫書籍
        const loadInventory = async () => {
            try {
                const res = await axios.get(`${API_BASE}/library/manage`);
                inventoryList.value = res.data;
            } catch (error) {
                console.error('庫存載入失敗，請聯絡後台');
                inventoryList.value = [
                    { inventoryId: 1, isbn: "9789571234561", status: "在庫" },
                    { inventoryId: 2, isbn: "9789571234562", status: "在庫" }
                ];
            }
        };

        // 尚無此功能(未來新增)
        // 🌟 初始化：同步獲取目前所有的借閱紀錄
        // const loadAllRecords = async () => {
        //     try {
        //         // 如果後端你有寫查詢總紀錄的 API
        //         const res = await axios.get(`${API_BASE}/library/records`);
        //         myBorrowingRecords.value = res.data;
        //     } catch (error) {
        //         console.warn('暫時查無借閱歷史');
        //     }
        // };

        // 點擊「確認借閱」
        const borrowBook = async (book) => {
            try {
                const res = await axios.post(`${API_BASE}/library/rent/${book.inventoryId}`, { withCredentials: true });
                const result = res.data;

                if (result.successful) {
                    alert(result.message || '借閱成功！');
                    book.status = '已借閱';
                    // 🌟 一行流更新：直接用後端打包好的最新總紀錄蓋掉舊畫面
                    borrowingRecords.value = result.data;
                } else {
                    alert(result.message || '借閱失敗！');
                }
            } catch (error) {
                alert('借閱失敗，請聯絡後台');
            }
        };

        // 點擊「確認歸還」
        const returnBook = async (record) => {
            try {
                const res = await axios.put(`${API_BASE}/library/return/${record.inventoryId}`, { withCredentials: true });
                const result = res.data;

                if (result.successful) {
                    alert(result.message || '歸還成功！');

                    const matchedBook = inventoryList.value.find(b => b.inventoryId === id);
                    if (matchedBook) matchedBook.status = '在庫';

                    borrowingRecords.value = result.data;
                } else {
                    alert(result.message || '歸還失敗！');
                }
            } catch (error) {
                alert('歸還失敗，請聯絡後台');
            }
        };

        // 登出(尚未解決session問題，未完成)
        const handleLogout = () => {
            alert('確定登出');
            location.href = './login.html';
        };

        // 網頁登入自動載入資料
        onMounted(() => {
            loadInventory();
        });

        return {
            currentUser,
            inventoryList,
            borrowingRecords,
            loadInventory,
            borrowBook,
            returnBook,
            handleLogout
        };
    }
}).mount('#app');