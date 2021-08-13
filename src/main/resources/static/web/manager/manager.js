
const app = Vue.createApp({
    data() {
        return {
            clientList: [],
            newClient: {
                name: "",
                lastName: "",
                email: "",
                password: "",
            },
            newLoan:{
                name:'',
                maxAmount:'',
                payments:'',
                percentage:'',
            },

            alertClass: {
                'visible': false,
            },
            alertLoan: {
                'visible': false,
            },
            loans:[],
        }
    },
    created() {
        this.loadData(0)
        this.fetchLoans()
        
    },
    computed:{
        currentPage(){
            return this.jsonData.page.number +1;
        },
        payments(){
            return JSON.parse('[' + this.newLoan.payments + ']');
        }, 
        
        
        
    },
    methods: {
        loadData(page) {
            let params = { page : page, size: 10 };
            axios
                // .get('/rest/clients', {params: params})
                .get('/api/clients')
                .then((response) => {
                    this.clientList = response.data;
                    console.log(this.clientList);
                    // this.tableContent = [...this.jsonData._embedded.clients];
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
                    this.showAlert(this.alertClass);
                    this.loadData();
                })
                .catch(error => {
                    console.log(error);
                });
        },
        addLoan(){
            axios.post('/api/loans',{
                  name: this.newLoan.name,
                  maxAmount: this.newLoan.maxAmount,  
                  payments: this.payments,
                   percentage: this.newLoan.percentage
              })
              .then(res => {
                  console.log(res);
                  this.newLoan.name = '';
                  this.newLoan.payments = '';
                  this.newLoan.percentage = '';
                  this.newLoan.maxAmount = '';
                  this.showAlert(this.alertLoan);
                  this.fetchLoans();

              })
              .catch(err => {
                  console.log(err.response);
                  alert(err.response.data);
                })
        },
        fetchLoans(){
            axios
            .get("/rest/loans")
            .then((response) => {
              console.log(response);
              this.loans = response.data._embedded.loans;
              this.loans.forEach((loan) => loan.payments.sort((a, b) => a - b)); // ordeno los payments para que se vean de menor a mayor
            })
            .catch((error) => console.log(error.response));
        },
        showAlert(classObj) {
            classObj.visible = true;
            setTimeout(() => { classObj.visible = false; }, 1600);
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
        logout() {
              axios.post("/api/logout").then((response) => {
              console.log(response)
              window.location = "/web/index.html";
              });
            },
        
    }
}).mount('#app');

