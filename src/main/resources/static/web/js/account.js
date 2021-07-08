const app = Vue.createApp({
    data() {
        return {
            accountData:{},
            transactions : [],
            navShow: false,
        }
    },
    created() {
        const urlSearchParams = new URLSearchParams(window.location.search);
        const params = Object.fromEntries(urlSearchParams.entries());
        this.fetchData(params);
    },
    methods: {
        fetchData(params) {
            
            axios.get('/api/clients/current/account', {params:params})
                .then(response =>{ 
                    this.accountData = response.data;
                    this.transactions = [...this.accountData.transactions];
                    this.transactions.sort((a,b)=>b.id - a.id)
                    console.log(this.transactions)
                   
                })
                .catch(error => console.log(error));
        },
        dateAndTime(creationdate){
            let date = new Date(creationdate);
            return date.toLocaleString();
        },
        
        objectClass(type){
            return{
                'bg-success': type == 'CREDIT',
                'bg-danger': type== 'DEBIT',
            }
        },
        toggleNav() {
            this.navShow = !this.navShow;
        },
        logout() {
            axios.post("/api/logout").then((response) => console.log(response));
            window.location = window.location.origin + '/web/index.html';
          },
    },
    computed:{
        hasTransactions(){
            return this.transactions.length > 0;
        }
    },

 
}).mount('#app');