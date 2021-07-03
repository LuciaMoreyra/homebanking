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
    separarNumeros(num) {
      if (num.length == 16) {
        return (
          num.slice(0, 4) +
          " " +
          num.slice(4, 8) +
          " " +
          num.slice(8, 12) +
          " " +
          num.slice(12)
        );
      }
    },
    logout() {
      axios.post("/api/logout").then((response) => console.log(response));
      window.location = window.location.origin + '/web/index.html';
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
  },
}).mount("#app");
