using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using TODO_Desktop.Models;
namespace TODO_Desktop.Api;

public class ApiClient
{
    private readonly HttpClient _httpClient;
    private string _jwtToken;
    private string _lastErrorMessage;

    public async Task<string> GetLastErrorMessageAsync()
    {
        return _lastErrorMessage;
    }

    public ApiClient()
    {
        _httpClient = new HttpClient();
        _httpClient.BaseAddress = new Uri("http://localhost:8080/");
    }

    public async Task<bool> RegisterAsync(string username, string email, string password)
    {
        try
        {
            var response = await _httpClient.PostAsJsonAsync("api/auth/register", new RegisterRequest
            {
                Username = username,
                Email = email,
                Password = password
            });

            Console.WriteLine($"Response Status: {response.StatusCode}");

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                try
                {
                    using var document = JsonDocument.Parse(errorContent);
                    _lastErrorMessage = document.RootElement.GetProperty("message").GetString();
                }
                catch
                {
                    _lastErrorMessage = "An error occurred while parsing the error message.";
                }
                return false;
            }
            _lastErrorMessage = null;
            return response.IsSuccessStatusCode;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Exception: {ex.Message}");
            return false;
        }
    }

    public async Task<bool> LoginAsync(string username, string password)
    {
        try
        {
            var response = await _httpClient.PostAsJsonAsync("api/auth/login", new LoginRequest
            {
                Username = username,
                Password = password
            });

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                try
                {
                    using var document = JsonDocument.Parse(errorContent);
                    _lastErrorMessage = document.RootElement.GetProperty("message").GetString();
                }
                catch
                {
                    _lastErrorMessage = "An error occurred while parsing the error message.";
                }
                return false;
            }

            var auth = await response.Content.ReadFromJsonAsync<AuthResponse>();
            _jwtToken = auth?.Token;

            _httpClient.DefaultRequestHeaders.Authorization =
                new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", _jwtToken);

            _lastErrorMessage = null; // Сбрасываем ошибку при успешном входе
            return true;
        }
        catch (Exception ex)
        {
            _lastErrorMessage = ex.Message;
            return false;
        }
    }

    public bool IsAuthorized => !string.IsNullOrEmpty(_jwtToken);

    // пример запроса с авторизацией
    public async Task<string> GetProtectedDataAsync()
    {
        var response = await _httpClient.GetAsync("api/protected");
        return await response.Content.ReadAsStringAsync();
    }
}