<template>
  <div class="modal fade" id="alertModal" tabindex="-1" role="dialog" v-if="message">
    <div class="modal-dialog" role="document">
      <div class="modal-content border-warning">
        <div class="modal-header bg-warning">
          <h5 class="modal-title" id="alertModalLabel">
            {{ caption }}
          </h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true" v-on:click="afterClose">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          {{ message }}
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal"
                  v-on:click="afterClose">Закрыть
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Alert",
    props: ["caption", "message", "whenApplied"],
    methods: {
      afterClose: function () {
        this.whenApplied();
        }
    }
  }
</script>

<style scoped>

</style>
