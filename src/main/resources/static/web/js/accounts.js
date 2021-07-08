const app = Vue.createApp({
  data() {
    return {
      clientData: [],
      navShow: false,
      loans: [],
      cards: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      axios
        .get('/api/clients/current')
        .then((response) => {
          this.clientData = response.data;
          this.clientData.accounts.sort((a, b) => b.id - a.id);
          this.loans = this.clientData.loans;
          this.cards = this.clientData.cards;
        })
        .catch((error) => console.log(error));
    },
    dateAndTime(creationdate) {
      let date = new Date(creationdate);
      return date.toLocaleDateString();
    },
    toggleNav() {
      this.navShow = !this.navShow;
    },
    goToUrl(id) {
      const actual = window.location.origin;
      window.location = actual + `/web/account.html?id=${id}`;
    },
    logout() {
      axios.post("/api/logout").then((response) => console.log(response));
      window.location = window.location.origin + '/web/index.html';
    },
    createAccount(){
    axios
           .post('/api/clients/current/accounts')
           .then(response => location.reload())
           .catch(error => console.log(error));
      },
      createCard(){
        window.location = window.location.origin + '/web/create-cards.html';
      },
    },

  computed: {
    hasLoans() {
      return this.loans.length > 0;
    },
    creditCards() {
      return this.cards.filter((card) => card.type == "CREDIT");
    },
    debitCards() {
      return this.cards.filter((card) => card.type == "DEBIT");
    },
    accountsNumber(){
    if (this.clientData.accounts != undefined){
     return this.clientData.accounts.length
    }
    },
    maxCards(){
      if (this.creditCards.length == 3 && this.debitCards.length == 3 ){
        return true
      }
      return false
    },

  },
}).mount("#app");
