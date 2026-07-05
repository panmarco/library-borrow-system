const { ref, onMounted } = Vue;

Vue.createApp({
    setup() {

        // 使用者資訊
        const currentUser = ref(null);

        // 所有庫存
        const inventoryList = ref([]);

        // 所有借閱紀錄
        const borrowingRecords = ref([]);

        // 獲取登入者資訊
        const loadUserInfo = async () => {
            try {
                const res = await axios.get(`/user/getUserInfo`);
                currentUser.value = res.data;
            } catch (error) {
                alert('資料載入失敗，請聯絡後台');
                location.href = './login.html';
            }
        };

        // 初始化：獲取所有資料
        const loadAllData = async () => {
            try {
                const res = await axios.get(`/library/manage`);

                inventoryList.value = res.data.inventories;
                borrowingRecords.value = res.data.records;
            } catch (error) {
                console.error('資料載入失敗，請聯絡後台');
            }
        };

        // 點擊「確認借閱」
        const borrowBook = async (book) => {
            try {
                const res = await axios.post(`/library/rent/${book.inventoryId}`);
                const result = res.data;

                if (result.successful) {
                    alert(result.message || '借閱成功！');
                    book.status = '已借閱';
                    location.reload();
                } else {
                    alert(result.message || '借閱失敗！');
                }
            } catch (error) {
                alert('借閱失敗，請聯絡後台');
            }
        };

        // 點擊「確認歸還」
        const returnBook = async (book) => {
            try {
                const res = await axios.post(`/library/return/${book.inventoryId}`);
                const result = res.data;

                if (result.successful) {
                    alert(result.message || '歸還成功！');
                    book.status = '在庫';
                    location.reload();
                } else {
                    alert(result.message || '歸還失敗！');
                }
            } catch (error) {
                alert('歸還失敗，請聯絡後台');
            }
        };

        // 登出
        const logout = async () => {
            const result = confirm('確定登出');
            if (result) {
                await axios.delete(`/user/logout`);
                location.href = './login.html';
            }
        };

        // 日期時間格式
        const formatTime = (timeString) => {
            if (!timeString) return '無';
            const date = new Date(timeString);

            // 如果時間解析失敗，直接回傳原字串
            if (isNaN(date.getTime())) return timeString;

            const y = date.getFullYear();
            const m = String(date.getMonth() + 1).padStart(2, '0');
            const d = String(date.getDate()).padStart(2, '0');
            const hh = String(date.getHours()).padStart(2, '0');
            const mm = String(date.getMinutes()).padStart(2, '0');

            return `${y}-${m}-${d} ${hh}:${mm}`;
        };

        // 網頁登入自動載入資料
        onMounted(async () => {
            await loadUserInfo();
            await loadAllData();
        });

        return {
            inventoryList,
            borrowingRecords,
            currentUser,
            loadUserInfo,
            loadAllData,
            borrowBook,
            returnBook,
            logout,
            formatTime
        };
    }
}).mount('#app');