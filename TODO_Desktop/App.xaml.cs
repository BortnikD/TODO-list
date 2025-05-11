using System.Windows;
using TODO_Desktop.Views;

namespace TODO_Desktop
{
    public partial class App : Application
    {
        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);
            SetTheme(IsSystemInDarkMode() ? "Dark" : "Light");
            var loginWindow = new LoginWindow();
            loginWindow.Show();
        }
        public void SetTheme(string theme)
        {
            var dict = new ResourceDictionary();
            dict.Source = new Uri($"Themes/{theme}Theme.xaml", UriKind.Relative);

            // Удаляем старую тему
            for (int i = Resources.MergedDictionaries.Count - 1; i >= 0; i--)
            {
                var md = Resources.MergedDictionaries[i];
                if (md.Source != null && (md.Source.OriginalString.Contains("LightTheme.xaml") || md.Source.OriginalString.Contains("DarkTheme.xaml")))
                {
                    Resources.MergedDictionaries.RemoveAt(i);
                }
            }
            Resources.MergedDictionaries.Add(dict);
        }
        public static bool IsSystemInDarkMode()
        {
            try
            {
                var key = Microsoft.Win32.Registry.CurrentUser.OpenSubKey(@"Software\Microsoft\Windows\CurrentVersion\Themes\Personalize");
                if (key != null && key.GetValue("AppsUseLightTheme") is int value)
                    return value == 0;
            }
            catch { }
            return false;
        }
    }
}