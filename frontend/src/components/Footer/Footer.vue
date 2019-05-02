<template>

  <div class="container-fluid py-4 mt-5 footer text-secondary" id="footer">
    <div class="row justify-content-center">
      <div class="col-10 col-sm-9 col-md-7 col-xl-5 col-lg-4">
        <div class="row align-items-center text-center">

          <footer-icon title="LinkedIn profile"
                       href="https://www.linkedin.com/in/oleksii-onishchenko"
                       icon-prefix="fab"
                       icon-name="linkedin">
          </footer-icon>
          <footer-icon title="Project Github repository"
                       href="https://github.com/JIeIIIa/codenames-boardgame-vue"
                       icon-prefix="fab"
                       icon-name="github-square">
          </footer-icon>
          <footer-icon title="Send email"
                       href="mailto:onishchenko.oleksii@gmail.com"
                       icon-prefix="fas"
                       icon-name="envelope">
          </footer-icon>
          <footer-icon title="Send message in Skype"
                       href="skype:Pro100JIeIIIa?chat"
                       icon-prefix="fab"
                       icon-name="skype">
          </footer-icon>
          <footer-icon title="Send message in Viber"
                       href="viber://add?number=380637923713"
                       icon-prefix="fab"
                       icon-name="viber">
          </footer-icon>
          <footer-icon title="Send message in Telegram"
                       href="tg://resolve?domain=JIeIIIa"
                       icon-prefix="fab"
                       icon-name="telegram-plane">
          </footer-icon>
        </div>
      </div>
    </div>

    <Copyright></Copyright>

  </div>
</template>

<script>
  import Copyright from "./Copyright";
  import FooterIcon from "./FooterIcon";

  export default {
    name: "Footer",
    components: {Copyright, FooterIcon}
  }
</script>

<style lang="scss">
  .link-unstyled {
    &, &:visited, &:hover,
    &:active, &:focus,
    &:active:hover {
      font-style: inherit;
      color: inherit;
      background-color: transparent;
      font-size: inherit;
      text-decoration: none;
      font-variant: inherit;
      font-weight: inherit;
      line-height: inherit;
      font-family: inherit;
      border-radius: inherit;
      border: inherit;
      outline: inherit;
      box-shadow: inherit;
      padding: inherit;
      vertical-align: inherit;
    }
  }

  $gray-300: #b2e3e9;
  $gray-800: #343a40;

  .footer {
    background: black;

    &-caption {
      color: $gray-300;
    }

    & &-social {
      transform: scale(1);
      transition-duration: 700ms;

      &:hover, &:focus {
        color: white;
        transform: scale(1.5);
        transition-duration: 300ms;
      }
    }
  }

</style>
