const app = Vue.createApp({
  data() {
    return {
      type: "debit",
      color: "gold",


    };
  },
  methods: {
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
      }).then((response) => {
          console.log(response)
           this.backToCards()

        }).catch(error=>
        {
        console.log(error)
        let myModal = new bootstrap.Modal(document.getElementById('modalError'), {
          keyboard: false
        })
        myModal.toggle()
        });
    },
    backToCards(){
        window.location = '/web/cards.html'
    }
  },
}).mount("#app");
