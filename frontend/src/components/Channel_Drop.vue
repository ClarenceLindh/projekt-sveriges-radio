<template>
    <select name="channels" id="channels_drop" @change="saveChannel(true)">
        <option value="" selected disabled hidden>Kanaler</option>
        <option  v-for="(channel, index) in getChannel" :key="index"  :value="channel.id" >  <!--false=boolean till episodeToggle-->
                {{channel.name}}
        </option>
    </select> 
</template>

<script>
export default {
    name: "Channels",

    computed: {
        getChannel(){
            return this.$store.getters.getChannel
        },
    },

 methods:{
      saveChannel(toggleEpisode){
            var e = document.getElementById("channels_drop");
            var strUser = e.options[e.selectedIndex].value;
            this.$store.commit('setToggleList',toggleEpisode);
            this.$store.commit('addChannelID',strUser);
            let today = new Date().toISOString().slice(0, 10)
            this.$store.commit('setDate', today)
            this.$store.dispatch("fetchEpisodesByChannel"),
            this.$store.dispatch("fetchProgram"),
            this.$store.commit('setChannelName', e.options[e.selectedIndex].text)

            var f = document.getElementById("category_drop");
            f.selectedIndex = 0;
      }
    },

   async mounted(){
        this.$store.dispatch("fetchChannels")
    },
}
</script>
