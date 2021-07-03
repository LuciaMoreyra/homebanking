
const app = Vue.createApp({
    data() {
        return {
            jsonData: {},
            tableContent: [],
            newClient: {
                name: "",
                lastName: "",
                email: "",
            },

            alertClass: {
                'visible': false,
            }
        }
    },
    created() {
        this.loadData(0)
        
    },
    computed:{
        currentPage(){
            return this.jsonData.page.number +1;
        }
    },
    methods: {
        loadData(page) {
            let params = { page : page, size: 20 };
            axios
                .get('/rest/clients', {params: params})
                .then((response) => {
                    this.jsonData = response.data;
                    this.tableContent = [...this.jsonData._embedded.clients];
                })
                .catch(error => {
                    console.log(error);
                })
        },
        addClient() {
            if (this.isInClientList() == false) {
                if (this.newClient.name.trim() || this.newClient.lastName.trim() && this.newClient.email) {
                    let clientToAdd = { ...this.newClient }
                    this.newClient.name = "";
                    this.newClient.lastName = "";
                    this.newClient.email = "";
                    this.postClient(clientToAdd)
                }
            }
            else {
                alert('this email is already registered')
            }

        },
        postClient(clientToAdd) {
            axios.post('/rest/clients', {
                firstName: clientToAdd.name.trim(),
                lastName: clientToAdd.lastName.trim(),
                email: clientToAdd.email.trim(),
            })
                .then(response => {
                    this.showAlert();
                    this.loadData();
                })
                .catch(error => {
                    console.log(error);
                });
        },
        showAlert() {
            this.alertClass.visible = true;
            setTimeout(() => { this.alertClass.visible = false; }, 1600);
        },
        isInClientList() {
            for (const client of this.tableContent) {
                if (client.email == this.newClient.email) {
                    return true;
                }
            }
            return false;
        },
        previousPage() {
            if (this.jsonData.page.number > 0) {
                this.loadData(this.jsonData.page.number - 1)
            }
        },
        nextPage() {
            if (this.jsonData.page.totalPages > this.currentPage) {
                this.loadData(this.jsonData.page.number + 1)
            }
        },
        goToPage(number) {
            if (this.jsonData.page.totalPages >= number && number > 0) {
                this.loadData(number-1)
            }
            
        },
    }
})
app.mount('#app');

