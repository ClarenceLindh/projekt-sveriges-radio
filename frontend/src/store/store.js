import { createStore} from "vuex";
import axios from 'axios';

export default createStore({
  name: 'store',

  state: {
    program: [],
    category:[],
    channel:[],
    episodes:[],
    channelId: 163,
    programId: 4821
  },

  mutations: {
    addChannelID(state,payload){
      state.channelId = payload;
    }
  ,  addProgramID(state,payload){
      state.programId = payload;
    }
  ,
    setProgram(state, payload){
      state.program = payload;
    },
    setAllCategories(state, payload){
      state.category=payload;
    },
    setAllEpisodes(state, payload){
      state.episodes=payload;
    },
    
      setChannel(state,payload){
        state.channel=payload
      }
    

    
  },

  //http://localhost:3000/rest/programs/channel/163 

  actions: {
    async fetchProgram(){
      await axios.get("http://localhost:3000/rest/programs/channel/" + this.state.channelId)
      .then(response => {
        this.commit("setProgram", response.data)
        console.log(response.data)
      })
    },
    async fetchChannels(){
      await axios.get("http://localhost:3000/rest/channels")
      .then(response => {
        this.commit("setChannel", response.data)
        console.log(response.data)
      })
    },
    
    async fetchAllCategories(){
      await axios.get("http://localhost:3000/rest/categories")
      .then(response => {
        this.commit("setAllCategories", response.data)
        console.log(response.data)
      })
    },

    async fetchAllEpisodes(){
      await axios.get("http://localhost:3000/rest/episodes/" + this.state.programId)
      .then(response => {
        this.commit("setAllEpisodes", response.data)
        console.log(response.data)
      })
    },

    async actionWithValue(store,data){
      console.log(data)
    }

  },

  getters: {
    getProgram(state){
      return state.program
    },
    getChannel(state){
      return state.channel
    },

    getAllCategories(state){
      return state.category
    },
    
    getAllEpisodes(state){
      console.log(state.episodes)
    return state.episodes
    },
    
    getChannelId(state) {
      return state.channelId
    }

},

  modules: {

  },

});
