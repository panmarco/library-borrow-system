const { ref } = Vue;

Vue.createApp({
    setup() {
        const registerForm = ref({
            phoneNumber: '',
            userName: '',
            password: ''
        });

        const handleRegister = async () => {
            try {
                const response = await axios.post('/user/register', registerForm.value);

                if (response.data.successful) {
                    alert(response.data.message);
                    location.href = './login.html';
                } else {
                    alert('註冊失敗：' + response.data.message);
                }
            } catch (error) {
                alert('網路或伺服器連線異常：' + error.message);
            }
        };

        const goToLogin = () => {
            location.href = './login.html';
        };

        return {
            registerForm,
            handleRegister,
            goToLogin
        };
    }
}).mount('#app');