<template>
  <div class="col-4 col-sm-2">
    <h2 class="footer-social"
        v-tooltip data-placement="top"
        :title="title">
      <a :href="href" class="link-unstyled">
        <font-awesome-icon :icon="[iconPrefix, iconName]"/>
      </a>
    </h2>
  </div>
</template>

<script>
  export default {
    name: "FooterIcon",
    props: ['href', 'title', 'iconPrefix', 'iconName']
  }
</script>

<style scoped>

</style>
