import { useState, type FormEvent } from 'react'
import './App.css'

interface LoanRateResponse {
  creditScore: number
  loanRate: number
}

function App() {
  const [creditScore, setCreditScore] = useState('')
  const [result, setResult] = useState<LoanRateResponse | null>(null)
  const [error, setError] = useState<string | null>(null)

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError(null)
    setResult(null)

    try {
      const response = await fetch(
        `http://localhost:8080/api/loan-rate?creditScore=${creditScore}`,
      )
      if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`)
      }
      const data: LoanRateResponse = await response.json()
      setResult(data)
    } catch {
      setError('Could not fetch loan rate. Is the backend running?')
    }
  }

  return (
    <div className="app">
      <h1>Loan Rate Matcher</h1>
      <form onSubmit={handleSubmit}>
        <label htmlFor="creditScore">Credit Score</label>
        <input
          id="creditScore"
          type="number"
          value={creditScore}
          onChange={(event) => setCreditScore(event.target.value)}
          required
        />
        <button type="submit">Get Loan Rate</button>
      </form>

      {result && (
        <p className="result">
          Credit score {result.creditScore} qualifies for a rate of {result.loanRate}%
        </p>
      )}

      {error && <p className="error">{error}</p>}
    </div>
  )
}

export default App
