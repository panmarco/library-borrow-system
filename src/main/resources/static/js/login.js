const { ref } = Vue;

Vue.createApp({
    setup() {
        const loginForm = ref({
            phoneNumber: '',
            password: ''
        });

        const handleLogin = async () => {
            try {
                const response = await axios.post(
                    '/user/login',
                    loginForm.value
                );

                if (response.data.successful) {
                    alert('登入成功！歡迎 ' + response.data.userName);

                    location.href = './library.html';
                } else {
                    alert('登入失敗：' + response.data.message);
                }

            } catch (error) {
                alert('網路或伺服器錯誤：' + error.message);
            }
        };

        const goToRegister = () => {
            location.href = './register.html';
        };

        return {
            loginForm,
            handleLogin,
            goToRegister
        };
    }
}).mount('#app');