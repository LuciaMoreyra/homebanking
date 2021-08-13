const app = Vue.createApp({
    data(){
        return {
            clientData: [],
            navShow: false,
            cards: [],
            cardDeleteId: "",
            type:'',
            color:'',
        }
    },
    created() {
        this.fetchData();
      },
    methods:{
        fetchData() {
            axios
              .get("/api/clients/current")
              .then((response) => {
                this.clientData = response.data;
                // this.clientData.accounts.sort((a, b) => b.id - a.id);
                // this.loans = this.clientData.loans;
                this.cards = this.clientData.cards;
                console.log(this.clientData.accounts);
              })
              .catch((error) => console.log(error));
          },
          deleteCard() {
            console.log("eliminar tarjeta");
            axios
              .delete("/api/clients/current/cards", {
                params: { cardId: this.cardDeleteId },
              })
              .then((res) => {
                console.log(res);
                this.fetchData();
              })
              .catch((err) => console.log(err.response));
          },
          createCard() {
           
            axios({
              method: "post",
              url: "/api/clients/current/cards",
              params: {
                type: this.type.toUpperCase(),
                color: this.color.toUpperCase(),
              },
              headers: {
                "content-type": "application/x-www-form-urlencoded",
              },
            })
              .then((response) => {
                console.log(response);
                this.fetchData();
              })
              .catch((error) => {
                console.log(error);
                
                
              });

          },
          isExpired(dateStr) {
            return new Date(dateStr) < new Date();
          },
          
          logout() {
            axios.post("/api/logout").then((response) => console.log(response));
            window.location = window.location.origin + "/web/index.html";
          },
          dateAndTime(creationdate) {
            let date = new Date(creationdate);
            return date.toLocaleDateString();
          },
          toggleNav() {
            this.navShow = !this.navShow;
          },

    },
    computed:{
        maxCards() {
            if (this.creditCards.length == 3 && this.debitCards.length == 3) {
              return true;
            }
            return false;
          },
          creditCards() {
            return this.cards.filter((card) => card.type == "CREDIT");
          },
          debitCards() {
            return this.cards.filter((card) => card.type == "DEBIT");
          },
    }
}).mount('#app')